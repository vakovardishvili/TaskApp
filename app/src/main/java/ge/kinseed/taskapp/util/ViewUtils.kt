package ge.kinseed.taskapp.util

import android.widget.TextView
import androidx.annotation.DrawableRes

fun TextView.setDrawableRight(@DrawableRes drawable: Int) {
    setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable, 0)
}