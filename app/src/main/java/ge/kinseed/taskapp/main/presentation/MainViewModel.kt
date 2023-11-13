package ge.kinseed.taskapp.main.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.kinseed.taskapp.base.BaseViewModel
import ge.kinseed.taskapp.main.domain.interactor.GetExchangeDataUseCase
import ge.kinseed.taskapp.main.domain.interactor.TransactionFeeHelper
import ge.kinseed.taskapp.main.presentation.model.Amount
import ge.kinseed.taskapp.main.presentation.model.ExchangeUiState
import ge.kinseed.taskapp.main.presentation.model.TransactionSuccess
import ge.kinseed.taskapp.main.presentation.model.mapper.ExchangeUiMapper
import ge.kinseed.taskapp.util.Event
import ge.kinseed.taskapp.util.formatAmount
import ge.kinseed.taskapp.util.observeOnMainSubscribeOnIo
import ge.kinseed.taskapp.util.postEvent
import io.reactivex.rxjava3.core.Observable
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.concurrent.TimeUnit
import javax.inject.Inject


interface MainViewModel {
    interface Input {
        fun selectCurrencyClicked(currencyFrom: Boolean)
        fun currencySelected(currency: String)
        fun onAmountChanged(amountText: String, fromAmount: Boolean)
        fun submitExchange()
    }

    interface Output {
        val loadingLiveData: LiveData<Boolean>
        val uiStateLiveData: LiveData<ExchangeUiState>
        val selectCurrencyClickedLiveData: LiveData<Event<List<String>>>
        val calculatedAmountLiveData: LiveData<Pair<String?, Boolean>>
        val exchangeErrorLiveData: LiveData<Event<String>>
        val transactionSuccessLiveData: LiveData<Event<TransactionSuccess>>
    }

    @HiltViewModel
    class ViewModel @Inject constructor(
        getExchangeData: GetExchangeDataUseCase,
        exchangeUiMapper: ExchangeUiMapper,
        private val transactionFeeHelper: TransactionFeeHelper
    ) : BaseViewModel(), Input, Output {

        val input: Input = this
        val output: Output = this

        private val uiState: ExchangeUiState
            get() = _uiStateLiveData.value!!

        private val balance: HashMap<String, BigDecimal>
            get() = _uiStateLiveData.value?.balance ?: hashMapOf()

        private var rates: LinkedHashMap<String, String> = linkedMapOf()

        private val _uiStateLiveData = MutableLiveData<ExchangeUiState>()
        override val uiStateLiveData: LiveData<ExchangeUiState>
            get() = _uiStateLiveData

        private val _calculatedAmountLiveData = MutableLiveData<Pair<String?, Boolean>>()
        override val calculatedAmountLiveData: LiveData<Pair<String?, Boolean>>
            get() = _calculatedAmountLiveData

        private val _selectCurrencyClickedLiveData = MutableLiveData<Event<List<String>>>()
        override val selectCurrencyClickedLiveData: LiveData<Event<List<String>>>
            get() = _selectCurrencyClickedLiveData

        private val _exchangeErrorLiveData = MutableLiveData<Event<String>>()
        override val exchangeErrorLiveData: LiveData<Event<String>>
            get() = _exchangeErrorLiveData

        private val _transactionSuccessLiveData = MutableLiveData<Event<TransactionSuccess>>()
        override val transactionSuccessLiveData: LiveData<Event<TransactionSuccess>>
            get() = _transactionSuccessLiveData

        private val _loadingLiveData = MutableLiveData<Boolean>()
        override val loadingLiveData: LiveData<Boolean>
            get() = _loadingLiveData


        private var selectingCurrencyFrom = false

        init {
            Observable.interval(5, TimeUnit.SECONDS).flatMap {
                getExchangeData()
            }.observeOnMainSubscribeOnIo()
                .doOnSubscribe {
                    _loadingLiveData.value = true
                }.subscribe({ exchangeData ->
                    rates = exchangeData.rates

                    if (balance.isEmpty()) {
                        _loadingLiveData.value = false
                        _uiStateLiveData.value = exchangeUiMapper.convert(exchangeData)
                    }
                }, { throwable ->
                    _loadingLiveData.value = false
                }).addToCompositeDisposable()
        }

        override fun selectCurrencyClicked(currencyFrom: Boolean) {
            selectingCurrencyFrom = currencyFrom
            _selectCurrencyClickedLiveData.postEvent(rates.keys.toList())
        }

        override fun currencySelected(currency: String) {
            if (selectingCurrencyFrom && currency == uiState.exchangeTo || !selectingCurrencyFrom && currency == uiState.exchangeFrom) {
                update {
                    copy(
                        exchangeFrom = uiState.exchangeTo, exchangeTo = uiState.exchangeFrom
                    )
                }
                calculateExchange()
                return
            }


            update {
                copy(
                    exchangeFrom = if (selectingCurrencyFrom) currency else uiState.exchangeFrom,
                    exchangeTo = if (selectingCurrencyFrom) uiState.exchangeTo else currency,
                )
            }
            calculateExchange()
        }

        override fun onAmountChanged(amountText: String, fromAmount: Boolean) {
            val amount = try {
                BigDecimal(amountText)
            } catch (e: Exception) {
                update {
                    copy(
                        exchangeAmount = null, receivedAmount = null
                    )
                }
                _calculatedAmountLiveData.value = Pair(
                    null, fromAmount
                )
                return
            }

            update {
                copy(
                    exchangeAmount = if (fromAmount) amount else uiState.exchangeAmount,
                    receivedAmount = if (fromAmount) uiState.receivedAmount else amount
                )
            }

            calculateExchange(fromAmount)
        }

        override fun submitExchange() {
            val exchangeAmount = uiState.exchangeAmount ?: return

            if (balance[uiState.exchangeFrom] == null || exchangeAmount > balance[uiState.exchangeFrom]) {
                _exchangeErrorLiveData.postEvent("Not enough balance")
                return
            }
            val transactionFee = transactionFeeHelper.getTransactionFee()
            val calculatedFee = exchangeAmount.multiply(BigDecimal(transactionFee))
            val receivedAmount = exchangeAmount.minus(calculatedFee).multiply(
                BigDecimal(uiState.rate!!.toDouble())
            )

            transactionFeeHelper.transactionMade()
            updateBalance(exchangeAmount, uiState.exchangeFrom, true)
            updateBalance(receivedAmount, uiState.exchangeTo, false)

            _transactionSuccessLiveData.postEvent(
                TransactionSuccess(
                    fromAmount = Amount(
                        amount = exchangeAmount, currency = uiState.exchangeFrom
                    ),
                    toAmount = Amount(amount = receivedAmount, currency = uiState.exchangeTo),
                    fee = calculatedFee
                )
            )

            update {
                copy(
                    exchangeAmount = null, receivedAmount = null
                )
            }
            calculateExchange()
        }

        private fun calculateExchange(fromAmount: Boolean = true) {
            val currAmount = (if (fromAmount) uiState.exchangeAmount else uiState.receivedAmount)

            val calculatedRate = if (fromAmount) {
                calculateRate(uiState.exchangeFrom, uiState.exchangeTo)
            } else {
                calculateRate(uiState.exchangeTo, uiState.exchangeFrom)
            }

            val calculatedAmount = currAmount?.multiply(calculatedRate)

            update {
                copy(
                    exchangeAmount = if (fromAmount) uiState.exchangeAmount else calculatedAmount,
                    receivedAmount = if (fromAmount) calculatedAmount else uiState.receivedAmount,
                    rate = calculatedRate?.toFloat()
                )
            }

            _calculatedAmountLiveData.value = Pair(
                calculatedAmount?.formatAmount(), fromAmount
            )
        }

        private fun calculateRate(
            fromCurrency: String, toCurrency: String
        ): BigDecimal? {
            val rateToEuro = rates[fromCurrency]?.toBigDecimal()
            val rateEuroToTarget = rates[toCurrency]?.toBigDecimal()

            return BigDecimal(1).divide(rateToEuro, 5, RoundingMode.HALF_UP)
                ?.multiply(rateEuroToTarget)
        }

        private fun updateBalance(amount: BigDecimal, currency: String, exchangeFrom: Boolean) {
            val newBalance = if (balance.containsKey(currency)) {
                balance.apply {
                    if (exchangeFrom) {
                        if (this[currency]!!.minus(amount) < BigDecimal.ONE) {
                            this.remove(currency)
                            return@apply
                        } else {
                            this[currency] = this[currency]!!.minus(amount)
                        }
                    } else {
                        this[currency] = this[currency]?.plus(amount) ?: amount
                    }
                }
            } else {
                balance.apply {
                    this[currency] = amount
                }
            }
            _uiStateLiveData.value = _uiStateLiveData.value?.copy(balance = newBalance)
        }

        private fun update(block: ExchangeUiState.() -> ExchangeUiState) {
            _uiStateLiveData.value = block(uiState)
        }
    }
}