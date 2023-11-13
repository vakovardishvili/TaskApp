package ge.kinseed.taskapp.main.domain.interactor

import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionFeeHelper @Inject constructor() {
    private val transactionCount: AtomicReference<Int> = AtomicReference(0)
    private val transactionFee: Double = 0.05
    private val freeTransactions: Int = 3
    private val freeEveryNthTransaction: Int = 5

    fun transactionMade() = transactionCount.set(transactionCount.get() + 1)

    fun getTransactionFee(): Double {
        val transactionCount = transactionCount.get()
        return if (transactionCount < freeTransactions) {
            0.0
        } else if (transactionCount % freeEveryNthTransaction == 0) {
            0.0
        } else {
            transactionFee
        }
    }
}