package ge.kinseed.taskapp.main.data.entity.mapper

import ge.kinseed.taskapp.main.data.entity.EmployeeEntity
import ge.kinseed.taskapp.main.domain.model.Employee
import javax.inject.Inject


class EmployeeMapper @Inject constructor() {
    fun convert(entities: List<EmployeeEntity>): List<Employee> = entities.map(::convert)

    private fun convert(entity: EmployeeEntity): Employee = with(entity) {
        Employee(
            //for visual purposes limiting id :)
            id = id.substring(0, 5),
            firstName = firstName,
            lastName = lastName,
            company = company?.name?.substring(0, 5),
            status = status?.substring(0, 5),
            fixedLinePhone = fixedLinePhone?.substring(0, 5),
            mobilePhone = mobilePhone?.substring(0, 5),
            email = email?.substring(0, 5),
            created = created?.substring(0, 5)
        )
    }
}