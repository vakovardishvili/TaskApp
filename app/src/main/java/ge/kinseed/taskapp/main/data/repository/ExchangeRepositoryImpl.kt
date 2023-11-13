package ge.kinseed.taskapp.main.data.repository

import dagger.Reusable
import ge.kinseed.taskapp.main.data.api.ExchangeApi
import ge.kinseed.taskapp.main.data.entity.mapper.ExchangeMapper
import ge.kinseed.taskapp.main.domain.model.Exchange
import ge.kinseed.taskapp.main.domain.repository.ExchangeRepository
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

@Reusable
class ExchangeRepositoryImpl @Inject constructor(
    private val api: ExchangeApi,
    private val mapper: ExchangeMapper
) : ExchangeRepository {
    override fun getExchangeData(): Observable<Exchange> {
        return api.getExchangeData()
            .map(mapper::convert)
    }
}