package ge.kinseed.taskapp.main.data.api

import ge.kinseed.taskapp.main.data.entity.ExchangeEntity
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET

interface ExchangeApi {
    @GET("tasks/api/currency-exchange-rates")
    fun getExchangeData(): Observable<ExchangeEntity>
}