package ge.kinseed.taskapp.main.presentation

import android.os.Bundle
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import dagger.hilt.android.AndroidEntryPoint
import ge.kinseed.taskapp.R
import ge.kinseed.taskapp.databinding.ActivityMainBinding
import ge.kinseed.taskapp.main.presentation.currency_selector.CurrencySelectorPopup
import ge.kinseed.taskapp.main.presentation.model.Amount
import ge.kinseed.taskapp.main.presentation.transaction_success.TransactionSuccessPopup
import ge.kinseed.taskapp.util.observeEvent

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel.ViewModel by viewModels()
    private val adapter: BalanceAdapter = BalanceAdapter()

    private var onFromTextChangedListener: TextWatcher? = null
    private var ontoTextChangedListener: TextWatcher? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        observeData()
        setUpViews()
        setFragmentResultListener()
    }

    private fun setFragmentResultListener() {
        supportFragmentManager.setFragmentResultListener(
            CurrencySelectorPopup.REQUEST_KEY_SELECTED_CURRENCY,
            this
        ) { _, bundle ->
            val selectedCurrency =
                bundle.getString(CurrencySelectorPopup.REQUEST_KEY_SELECTED_CURRENCY)
            viewModel.input.currencySelected(selectedCurrency!!)
        }
    }

    private fun setUpViews() {
        binding.recyclerView.adapter = adapter

        binding.exchangeFromContainer.setOnClickListener {
            viewModel.input.selectCurrencyClicked(true)
        }
        binding.exchangeToContainer.setOnClickListener {
            viewModel.input.selectCurrencyClicked(false)
        }
        onFromTextChangedListener = binding.fromAmount.doOnTextChanged { text, _, _, _ ->
            viewModel.input.onAmountChanged(text.toString(), true)
        }

        ontoTextChangedListener = binding.toAmount.doOnTextChanged { text, _, _, _ ->
            viewModel.input.onAmountChanged(text.toString(), false)
        }

        binding.submit.setOnClickListener {
            viewModel.input.submitExchange()
        }
    }

    private fun observeData() {
        viewModel.output.uiStateLiveData.observe(this) { uiState ->
            adapter.submitList(uiState.balance.map {
                Amount(
                    amount = it.value,
                    currency = it.key
                )
            })

            binding.fromCurrency.text = uiState.exchangeFrom
            binding.toCurrency.text = uiState.exchangeTo
            binding.fromCurrencySymbol.text = uiState.fromCurrencySymbol
            binding.toCurrencySymbol.text = uiState.toCurrencySymbol
            binding.exchangeRate.text = getString(R.string.exchange_rate, uiState.rate)
        }

        viewModel.output.selectCurrencyClickedLiveData.observeEvent(this) { currencies ->
            CurrencySelectorPopup.newInstance(currencies).show(
                supportFragmentManager,
                CurrencySelectorPopup::class.java.simpleName
            )
        }

        viewModel.output.calculatedAmountLiveData.observe(this) { (amount, fromAmount) ->
            if (amount != null) {
                updateAmount(amount, fromAmount)
            } else {
                updateAmount("", true)
                updateAmount("", false)
            }
        }

        viewModel.output.exchangeErrorLiveData.observeEvent(this) { error ->
            showToast(error)
        }

        viewModel.output.transactionSuccessLiveData.observeEvent(this) { transactionSuccess ->
            TransactionSuccessPopup.newInstance(transactionSuccess).show(
                supportFragmentManager,
                TransactionSuccessPopup::class.java.simpleName
            )
        }

        viewModel.output.loadingLiveData.observe(this) { isLoading ->
            binding.progressCircular.isVisible = isLoading
        }
    }

    private fun updateAmount(text: String, fromAmount: Boolean) {
        if (fromAmount) {
            binding.toAmount.removeTextChangedListener(ontoTextChangedListener)
            binding.toAmount.setText(text)
            binding.toAmount.addTextChangedListener(ontoTextChangedListener)
        } else {
            binding.fromAmount.removeTextChangedListener(onFromTextChangedListener)
            binding.fromAmount.setText(text)
            binding.fromAmount.addTextChangedListener(onFromTextChangedListener)
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}