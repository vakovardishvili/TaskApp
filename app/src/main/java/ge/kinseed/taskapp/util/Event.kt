package ge.kinseed.taskapp.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

data class Event<out T>(private val content: T) {
    private var hasBeenHandled = false
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    fun peekContent(): T = content
}

fun <T> LiveData<Event<T>>.observeEvent(owner: LifecycleOwner, block: (T) -> Unit) {
    this.observe(owner) { event ->
        event.getContentIfNotHandled()?.let(block)
    }
}