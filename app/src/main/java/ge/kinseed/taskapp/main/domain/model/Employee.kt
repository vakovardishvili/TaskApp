package ge.kinseed.taskapp.main.domain.model

import kotlin.random.Random

data class Employee(
    val id: String,
    val firstName: String,
    val lastName: String,
    val company: String?,
    val status: String?,
    val fixedLinePhone: String?,
    val mobilePhone: String?,
    val email: String?,
    val created: String?
)