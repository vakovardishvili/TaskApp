package ge.kinseed.taskapp.view

import android.content.Context
import android.util.AttributeSet
import android.widget.TableRow
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import ge.kinseed.taskapp.R

class MyTableRow @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : TableRow(context, attributeSet) {

    var type: TableRowType = TableRowType.NORMAL
        set(value) {
            field = value
            setBackgroundColor(ContextCompat.getColor(context, value.backgroundColor))
        }

}

enum class TableRowType(
    @ColorRes
    val backgroundColor: Int
) {
    HEADER(
        R.color.table_header
    ),
    NORMAL(
        R.color.white
    ),
    HIGHLIGHT(
        R.color.table_highlight
    )
}