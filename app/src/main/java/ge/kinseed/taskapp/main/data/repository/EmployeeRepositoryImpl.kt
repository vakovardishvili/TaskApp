package ge.kinseed.taskapp.main.data.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import ge.kinseed.taskapp.main.data.entity.EmployeeEntity
import ge.kinseed.taskapp.main.data.entity.EmployeesEntity
import ge.kinseed.taskapp.main.data.entity.mapper.EmployeeMapper
import ge.kinseed.taskapp.main.domain.model.Employee
import ge.kinseed.taskapp.main.domain.repository.EmployeeRepository
import javax.inject.Inject

class EmployeeRepositoryImpl @Inject constructor(
    @ApplicationContext val context: Context,
    private val mapper: EmployeeMapper
) : EmployeeRepository {

    override fun getEmployees(): List<Employee> {
        val employees = context.assets.open(EMPLOYEE_DATA_ASSET_NAME)
            .bufferedReader()
            .use {
                val type = object : TypeToken<EmployeesEntity>() {}.type
                return@use Gson().fromJson<EmployeesEntity>(it.readText(), type)
            }

        return mapper.convert(employees.data)
    }

    companion object {
        private const val EMPLOYEE_DATA_ASSET_NAME = "test-data.json"
    }
}