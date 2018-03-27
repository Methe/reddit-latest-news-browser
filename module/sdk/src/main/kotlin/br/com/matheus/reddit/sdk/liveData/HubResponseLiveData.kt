package br.com.matheus.reddit.sdk.liveData

import android.arch.lifecycle.MediatorLiveData
import br.com.matheus.reddit.sdk.async
import br.com.matheus.reddit.sdk.extension.loadingResponse
import br.com.matheus.reddit.sdk.extension.toDataResponse
import br.com.matheus.reddit.sdk.extension.toErrorResponse
import br.com.matheus.reddit.sdk.model.type.ERROR
import br.com.matheus.reddit.sdk.model.type.LOADING
import br.com.matheus.reddit.sdk.model.type.SUCCESS

class HubResponseLiveData<T> : ResponseLiveData<T>() {

    private val sourceLiveData = MediatorLiveData<Any>()
    private val sourceObserver: (Any?) -> Unit = {}
    private var lastSource: ResponseLiveData<*>? = null

    val hasDataSource: Boolean
        get() = lastSource != null

    fun swapSource(source: ResponseLiveData<T>) {
        lastSource?.let { sourceLiveData.removeSource(it) }
        sourceLiveData.addSource(source, this::setValue)
        lastSource = source
    }

    fun <R> swapSource(source: ResponseLiveData<R>, transformation: (R) -> T) {
        lastSource?.let { sourceLiveData.removeSource(it) }
        sourceLiveData.addSource(source) { data ->
            async {
                if (data == null) return@async
                val newValue = when (data.resultStatus) {
                    SUCCESS -> data.data?.let(transformation).toDataResponse(SUCCESS)
                    ERROR -> data.error?.toErrorResponse()
                    LOADING -> loadingResponse()
                    else -> null
                }
                if (value != newValue) postValue(newValue)
            }
        }
        lastSource = source
    }

    override fun onActive() {
        super.onActive()
        if (!sourceLiveData.hasObservers()) sourceLiveData.observeForever(sourceObserver)
    }

    override fun onInactive() {
        super.onInactive()
        sourceLiveData.removeObserver(sourceObserver)
    }

    override fun compute() = Unit

    override fun invalidate() {
        super.invalidate()
        lastSource?.invalidate()
    }

}
