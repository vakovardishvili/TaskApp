package ge.kinseed.taskapp.main.presentation.model.mapper

import ge.kinseed.taskapp.main.domain.model.Exchange
import ge.kinseed.taskapp.main.presentation.model.ExchangeUiState
import java.math.BigDecimal
import javax.inject.Inject

class ExchangeUiMapper @Inject constructor() {
    fun convert(
        model: Exchange
    ): ExchangeUiState = with(model) {
        val currency = rates.keys.first { it != base }

        return@with ExchangeUiState(
            balance = hashMapOf(
                base to BigDecimal(1000)
            ),
            exchangeFrom = base,
            exchangeTo = currency,
            rate = rates[currency]?.toFloat() ?: 0.0f
        )
    }
}