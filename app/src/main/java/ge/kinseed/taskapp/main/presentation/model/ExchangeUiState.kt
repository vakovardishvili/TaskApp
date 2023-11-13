package ge.kinseed.taskapp.main.presentation.model

import ge.kinseed.taskapp.util.CurrencyConsts
import java.math.BigDecimal

data class ExchangeUiState(
    val balance: HashMap<String, BigDecimal>,
    val exchangeFrom: String,
    val exchangeTo: String,
    val exchangeAmount: BigDecimal? = null,
    val receivedAmount: BigDecimal? = null,
    val rate: Float?
) {
    val fromCurrencySymbol: String
        get() = CurrencyConsts.currencySymbolMap[exchangeFrom] ?: exchangeFrom

    val toCurrencySymbol: String
        get() = CurrencyConsts.currencySymbolMap[exchangeTo] ?: exchangeTo
}