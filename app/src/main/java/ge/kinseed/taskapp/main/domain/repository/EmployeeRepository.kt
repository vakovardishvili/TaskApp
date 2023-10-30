package ge.kinseed.taskapp.main.domain.repository

import ge.kinseed.taskapp.main.domain.model.Employee

interface EmployeeRepository {
    fun getEmployees(): List<Employee>
}