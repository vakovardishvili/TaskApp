package ge.kinseed.taskapp.main.domain.model

data class Exchange(
    val base: String,
    val rates: LinkedHashMap<String, String>
)
