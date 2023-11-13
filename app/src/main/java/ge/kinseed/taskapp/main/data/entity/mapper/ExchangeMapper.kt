package ge.kinseed.taskapp.main.data.entity.mapper

import dagger.Reusable
import ge.kinseed.taskapp.main.data.entity.ExchangeEntity
import ge.kinseed.taskapp.main.domain.model.Exchange
import javax.inject.Inject

@Reusable
class ExchangeMapper @Inject constructor() {
    fun convert(entity: ExchangeEntity): Exchange = with(entity) {
        Exchange(
            base = base,
            rates = rates
        )
    }
}