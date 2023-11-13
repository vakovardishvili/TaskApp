package ge.kinseed.taskapp.main.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal

@Parcelize
data class Amount(
    val amount: BigDecimal, val currency: String
) : Parcelable

@Parcelize
data class TransactionSuccess(
    val fromAmount: Amount, val toAmount: Amount, val fee: BigDecimal
) : Parcelable