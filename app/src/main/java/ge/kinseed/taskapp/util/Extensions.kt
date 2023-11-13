package ge.kinseed.taskapp.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.math.BigDecimal
import java.math.RoundingMode

fun <T : Any> Observable<T>.observeOnMainSubscribeOnIo() =
    observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())

fun <T> LiveData<Event<T>>.observeEvent(owner: LifecycleOwner, block: (T) -> Unit) {
    this.observe(owner) { event ->
        event.getContentIfNotHandled()?.let(block)
    }
}

fun <T> MutableLiveData<Event<T>>.postEvent(value: T) {
    postValue(Event(value))
}


fun BigDecimal.formatAmount(): String {
    return this.setScale(2, RoundingMode.FLOOR).toString()
}