package ge.kinseed.taskapp.main.domain.interactor

import dagger.Reusable
import ge.kinseed.taskapp.main.domain.repository.EmployeeRepository
import javax.inject.Inject

@Reusable
class GetEmployeesUseCase @Inject constructor(
    private val repository: EmployeeRepository
) {
    operator fun invoke() = repository.getEmployees()
}