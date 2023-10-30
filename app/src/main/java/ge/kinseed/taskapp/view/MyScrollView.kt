package ge.kinseed.taskapp.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ScrollView
import ge.kinseed.taskapp.view.listener.CustomOnScrollChangedListener

class MyScrollView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttributeSet: Int = 0
) : ScrollView(context, attributeSet, defStyleAttributeSet) {

    private var onScrollChangeListener: CustomOnScrollChangedListener? = null

    fun setCustomScrollChangeListener(listener: CustomOnScrollChangedListener) {
        onScrollChangeListener = listener
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        onScrollChangeListener?.onScrollChanged(this, l, t)
    }
}