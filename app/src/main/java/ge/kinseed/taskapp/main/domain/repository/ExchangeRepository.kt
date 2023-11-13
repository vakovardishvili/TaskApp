package ge.kinseed.taskapp.main.domain.repository

import ge.kinseed.taskapp.main.domain.model.Exchange
import io.reactivex.rxjava3.core.Observable

interface ExchangeRepository {
    fun getExchangeData(): Observable<Exchange>
}