package ge.kinseed.taskapp.main.domain.interactor

import dagger.Reusable
import ge.kinseed.taskapp.main.domain.repository.ExchangeRepository
import javax.inject.Inject

@Reusable
class GetExchangeDataUseCase @Inject constructor(
    private val repository: ExchangeRepository
) {
    operator fun invoke() = repository.getExchangeData()
}