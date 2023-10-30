package ge.kinseed.taskapp.view.listener

import android.view.View

fun interface CustomOnScrollChangedListener {
    fun onScrollChanged(self: View, x: Int, y: Int)
}