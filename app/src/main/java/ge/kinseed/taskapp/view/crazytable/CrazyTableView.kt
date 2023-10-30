package ge.kinseed.taskapp.view.crazytable

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TableLayout
import android.widget.TextView
import androidx.core.view.updateLayoutParams
import ge.kinseed.taskapp.databinding.ViewCrazyTableBinding
import ge.kinseed.taskapp.util.setDrawableRight
import ge.kinseed.taskapp.view.MyTableRow
import ge.kinseed.taskapp.view.TableRowType
import ge.kinseed.taskapp.view.listener.CustomOnScrollChangedListener
import ge.kinseed.taskapp.view.listener.OnCellClickListener

class CrazyTableView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttributeSet: Int = 0
) : FrameLayout(context, attributeSet, defStyleAttributeSet) {

    private val binding: ViewCrazyTableBinding =
        ViewCrazyTableBinding.inflate(LayoutInflater.from(context), this, true)

    var onCellClickListener: OnCellClickListener? = null

    lateinit var data: CrazyTableData

    init {
        syncTableScrolls()
    }

    fun setUpTable(data: CrazyTableData) {
        clearTable()
        this.data = data

        binding.tableLeftCorner.addRow {
            type = TableRowType.HEADER
            addTextView(data.mainProperty)
        }

        binding.tableLeftItems.apply {
            data.items.forEachIndexed { index, map ->
                addRow {
                    type = if (index % 2 == 0)
                        TableRowType.NORMAL else TableRowType.HIGHLIGHT
                    map[data.mainProperty]?.let { addTextView(it) }
                }
            }
        }

        val headersWithOutMainProperty = data.items.first().keys.filter {
            it != data.mainProperty
        }.toList()

        binding.tableTopItems.apply {
            addRow {
                type = TableRowType.HEADER
                headersWithOutMainProperty.forEach { title ->
                    addTextView(title)
                }
            }
        }
        data.items.forEachIndexed { index, map ->
            binding.tableMain.addRow {
                type = if (index % 2 == 0)
                    TableRowType.NORMAL else TableRowType.HIGHLIGHT
                headersWithOutMainProperty.forEach { title ->
                    map[title]?.let { addTextView(it) }
                }
            }
        }
    }

    private fun clearTable() {
        listOf(
            binding.tableLeftItems,
            binding.tableMain,
            binding.tableTopItems,
            binding.tableLeftCorner,
        ).forEach { it.removeAllViews() }
    }

    private fun syncTableScrolls() {
        val customVerticalScrollChangeListener =
            CustomOnScrollChangedListener { self, _, y ->
                if (self == binding.scrollViewMainVertical) {
                    binding.scrollViewLeftItems.scrollTo(0, y)
                } else {
                    binding.scrollViewMainVertical.scrollTo(0, y)
                }
            }
        val customHorizontalScrollChangeListener =
            CustomOnScrollChangedListener { self, x, _ ->
                if (self == binding.scrollViewMainHorizontal) {
                    binding.scrollViewTopItems.scrollTo(x, 0)
                } else {
                    binding.scrollViewMainHorizontal.scrollTo(x, 0)
                }
            }

        binding.scrollViewMainHorizontal.setCustomScrollChangeListener(
            customHorizontalScrollChangeListener
        )
        binding.scrollViewTopItems.setCustomScrollChangeListener(
            customHorizontalScrollChangeListener
        )
        binding.scrollViewMainVertical.setCustomScrollChangeListener(
            customVerticalScrollChangeListener
        )
        binding.scrollViewLeftItems.setCustomScrollChangeListener(
            customVerticalScrollChangeListener
        )
    }

    private fun TableLayout.addRow(block: MyTableRow.() -> Unit) {
        this.addView(MyTableRow(context).apply {
            block()
        })
    }

    private fun MyTableRow.addTextView(title: String, block: TextView.() -> Unit = {}) {
        val textView = TextView(context)
        addView(textView)

        textView.apply {
            text = title
            gravity = Gravity.CENTER
            updateLayoutParams {
                width = 300
                height = 150
            }
            if (type == TableRowType.HEADER) {
                if (data.sortingData?.title == title) {
                    setDrawableRight(data.sortingData!!.order.icon)
                }
                setOnClickListener {
                    onCellClickListener?.onCellClick(title)
                }
            }
            block()
        }
    }
}