package ge.kinseed.taskapp.main.data.entity

import com.google.gson.annotations.SerializedName

data class EmployeesEntity(
    val data: List<EmployeeEntity>
)

data class EmployeeEntity(
    @SerializedName("id")
    val id: String,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("clientOrganisation")
    val company: CompanyEntity?,
    val status: String?,
    val fixedLinePhone: String?,
    val mobilePhone: String?,
    val email: String?,
    val created: String?
)

data class CompanyEntity(
    val id: String,
    val name: String,
)