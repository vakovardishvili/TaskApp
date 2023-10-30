package ge.kinseed.taskapp.view.crazytable

import androidx.annotation.DrawableRes
import ge.kinseed.taskapp.R

data class CrazyTableData(
    val items: List<HashMap<String, String>>,
    val mainProperty: String,
    val sortingData: SortingData? = null
)

data class SortingData(
    val title: String,
    val order: SortOrder
)

enum class SortOrder(
    @DrawableRes val icon: Int
) {
    DESC(R.drawable.down_arrow), ASC(R.drawable.up_arrow);

    companion object {
        fun oppositeOrder(order: SortOrder?): SortOrder =
            if (order == null) ASC else if (order == DESC) ASC else DESC
    }
}