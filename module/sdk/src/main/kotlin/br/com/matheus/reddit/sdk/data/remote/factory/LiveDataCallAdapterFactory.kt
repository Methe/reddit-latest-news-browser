package br.com.matheus.reddit.sdk.data.remote.factory

import br.com.matheus.reddit.sdk.extension.loadingResponse
import br.com.matheus.reddit.sdk.extension.makeRequest
import br.com.matheus.reddit.sdk.extension.toDataResponse
import br.com.matheus.reddit.sdk.liveData.ResponseLiveData
import br.com.matheus.reddit.sdk.model.type.SUCCESS
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

internal class LiveDataCallAdapterFactory : CallAdapter.Factory() {

    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
        if (getRawType(returnType) != ResponseLiveData::class.java) return null

        val type = getParameterUpperBound(0, returnType as ParameterizedType)
        return LiveDataCallAdapter<Any>(type)
    }
}

internal class LiveDataCallAdapter<RESULT>(private val responseType: Type) : CallAdapter<RESULT, ResponseLiveData<RESULT>> {

    override fun responseType() = responseType

    override fun adapt(call: Call<RESULT>) = object : ResponseLiveData<RESULT>() {
        override fun compute() {
            postValue(loadingResponse())
            val result = call.makeRequest()
            postValue(result.toDataResponse(SUCCESS))
        }
    }
}