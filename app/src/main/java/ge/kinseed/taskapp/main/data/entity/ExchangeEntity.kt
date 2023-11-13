package ge.kinseed.taskapp.main.data.entity

import androidx.annotation.Keep

@Keep
data class ExchangeEntity(
    val base: String,
    val rates: LinkedHashMap<String, String>
)
