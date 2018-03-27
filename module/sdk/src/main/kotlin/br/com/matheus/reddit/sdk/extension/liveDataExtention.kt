@file:JvmName("LiveDataUtils")

package br.com.matheus.reddit.sdk.extension

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.Observer
import br.com.matheus.reddit.sdk.liveData.HubResponseLiveData
import br.com.matheus.reddit.sdk.liveData.ResponseLiveData
import br.com.matheus.reddit.sdk.model.Page
import br.com.matheus.reddit.sdk.model.Result

fun <T> LiveData<T>.observe(owner: LifecycleOwner, observer: (T) -> Unit) = observe(owner, Observer { it?.let(observer) })

fun <T> LiveData<T>.observeSingle(owner: LifecycleOwner, observer: ((T) -> Unit)) = observeUntil(owner) {
    it.let(observer)
    true
}

fun <T> LiveData<T>.observeUntil(owner: LifecycleOwner, observer: ((T) -> Boolean)) {
    observe(owner, object : Observer<T> {
        override fun onChanged(data: T?) {
            if (data?.let(observer) == true) removeObserver(this)
        }
    })
}

fun <T> MediatorLiveData<T>.addSource(source: LiveData<T>) = addSource(source) {
    value = it
}

fun <T> MediatorLiveData<T>.addSources(vararg sources: LiveData<T>, observer: (T?) -> Unit) {
    sources.forEach {
        addSource(it, observer)
    }
}

fun <T> ResponseLiveData<T>.onNext(action: (T) -> Unit): ResponseLiveData<T> {
    interceptData = {
        it?.data?.let(action)
    }
    return this
}

fun <T, R> ResponseLiveData<T>.map(transformation: (T) -> R): ResponseLiveData<R> {
    val hub = HubResponseLiveData<R>()
    hub.swapSource(this, transformation)
    return hub
}

internal fun <T> ResponseLiveData<Result<T>>.extractResult() = map(Result<T>::data)
internal fun <T> ResponseLiveData<Page<Result<T>>>.extractResultFromPage() = map { it.extractResult() }

