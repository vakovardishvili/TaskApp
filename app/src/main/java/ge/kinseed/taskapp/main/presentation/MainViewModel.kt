package ge.kinseed.taskapp.main.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.kinseed.taskapp.main.domain.interactor.GetEmployeesUseCase
import ge.kinseed.taskapp.main.domain.model.Employee
import ge.kinseed.taskapp.util.Event
import ge.kinseed.taskapp.view.crazytable.CrazyTableData
import ge.kinseed.taskapp.view.crazytable.SortOrder
import ge.kinseed.taskapp.view.crazytable.SortingData
import javax.inject.Inject
import kotlin.reflect.full.memberProperties

interface MainViewModel {
    interface Input {
        fun sortByCell(title: String)
        fun search(searchWord: String)
        fun filterButtonClicked()
        fun clearFilter()
    }

    interface Output {
        val tableLiveData: LiveData<CrazyTableData>
        val infoLiveData: LiveData<Event<String>>
    }

    @HiltViewModel
    class ViewModel @Inject constructor(
        getEmployees: GetEmployeesUseCase
    ) : androidx.lifecycle.ViewModel(), Input, Output {

        val input: Input = this
        val output: Output = this

        override val tableLiveData = MutableLiveData<CrazyTableData>()
        override val infoLiveData = MutableLiveData<Event<String>>()

        private var tableData: CrazyTableData
            get() = tableLiveData.value!!

        private var filterData = FilterData()

        init {
            val employeeData =
                getEmployees().map { employee ->
                    linkedMapOf<String, String>().apply {
                        Employee::class.memberProperties.forEach { property ->
                            this[property.name] = property.get(employee).toString()
                        }
                    }
                }

            tableData = CrazyTableData(
                //increase item count so beautiful vertical scroll is visible
                items = employeeData + employeeData.filter {it.containsKey("id")} + employeeData.filter {it.containsKey("id")},
                mainProperty = EMPLOYEE_MAIN_PROPERTY
            ).also { tableLiveData.postValue(it) }
        }

        override fun sortByCell(title: String) {
            val sortingData =
                SortingData(
                    title,
                    if (tableData.sortingData == null || tableData.sortingData?.title != title) {
                        SortOrder.ASC
                    } else SortOrder.oppositeOrder(tableData.sortingData?.order)
                )

            tableLiveData.postValue(
                tableData.copy(
                    items = tableData.items.sort(sortingData),
                    sortingData = sortingData
                )
            )
        }

        override fun search(searchWord: String) {
            filterData.searchWord = searchWord
        }

        override fun filterButtonClicked() {
            val filteredData = tableData.copy(
                items = tableData.items.filter {
                    filterData.searchWord?.let { word ->
                        it.values.any { value ->
                            value.contains(word, ignoreCase = true)
                        }
                    } ?: kotlin.run { true }
                },
            )

            if (filteredData.items.isNotEmpty()) {
                tableLiveData.postValue(
                    filteredData
                )
            } else {
                infoLiveData.postValue(Event("No matching data"))
            }
        }

        override fun clearFilter() {
            filterData.clearFilter()
            filterButtonClicked()
        }

        private fun List<HashMap<String, String>>.sort(
            sortingData: SortingData?
        ): List<HashMap<String, String>> {
            if (sortingData == null) return this

            return if (sortingData.order == SortOrder.ASC) {
                sortedBy { it[sortingData.title] }
            } else {
                sortedByDescending { it[sortingData.title] }
            }
        }
    }

    companion object {
        private const val EMPLOYEE_MAIN_PROPERTY = "id"
    }
}

data class FilterData(
    var searchWord: String? = null
) {
    fun clearFilter() {
        searchWord = null
    }
}