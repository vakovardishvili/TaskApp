package ge.kinseed.taskapp.main.data.entity.mapper

import ge.kinseed.taskapp.main.data.entity.EmployeeEntity
import ge.kinseed.taskapp.main.domain.model.Employee
import javax.inject.Inject


class EmployeeMapper @Inject constructor() {
    fun convert(entities: List<EmployeeEntity>): List<Employee> = entities.map(::convert)

    private fun convert(entity: EmployeeEntity): Employee = with(entity) {
        Employee(
            id = id.substring(0, 5),
            firstName = firstName,
            lastName = lastName,
            company = company?.name,
            status = status,
            fixedLinePhone = fixedLinePhone,
            mobilePhone = mobilePhone,
            email = email,
            created = created
        )
    }
}