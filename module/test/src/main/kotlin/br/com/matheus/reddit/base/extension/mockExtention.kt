package br.com.matheus.reddit.base.extension

import br.com.concretesolutions.kappuccino.utils.doWait
import br.com.matheus.reddit.sdk.liveData.ResponseLiveData
import br.com.matheus.reddit.sdk.model.DataResponse
import br.com.matheus.reddit.sdk.model.type.ERROR
import br.com.matheus.reddit.sdk.model.type.LOADING
import br.com.matheus.reddit.sdk.model.type.SUCCESS
import com.nhaarman.mockito_kotlin.whenever
import net.vidageek.mirror.dsl.Mirror
import org.mockito.Mockito
import java.lang.reflect.*
import java.lang.reflect.Array

fun <T> mockRepository(aClass: Class<T>): T {
    val mockedRepository = Mockito.mock(aClass) { invocation ->
        if (invocation.method.returnType != ResponseLiveData::class.java) invocation.callRealMethod()
        else {
            val parameterizedType = invocation.method.genericReturnType as ParameterizedType
            typedResponseLiveData(getRawType(parameterizedType.actualTypeArguments[0]))
        }
    }
    Mirror().on(aClass).set().field("INSTANCE").withValue(mockedRepository)
    return mockedRepository
}

fun <T> ResponseLiveData<T>?.mockCreation(value: T) = mockCreation(DataResponse(value, null, SUCCESS))

fun <T> ResponseLiveData<T>?.mockCreation(value: DataResponse<T>? = null): ResponseLiveData<T> {
    val liveData: ResponseLiveData<T> = object : ResponseLiveData<T>() {
        override fun compute() = Unit
    }
    whenever(this).thenReturn(liveData)

    if (value != null)
        Mirror().on(liveData).invoke().method("postValue").withArgs(value)
    return liveData
}

fun <T> ResponseLiveData<T>?.mockValue(value: T) = mockResponse(DataResponse(value, null, SUCCESS)).data!!

fun <T> ResponseLiveData<T>?.mockLoading() = mockResponse(DataResponse(null, null, LOADING))
fun <T> ResponseLiveData<T>?.mockError(error: Throwable) = mockResponse(DataResponse(null, error, ERROR)).error!!
fun <T> ResponseLiveData<T>?.mockResponse(value: DataResponse<T>): DataResponse<T> {
    Mirror().on(this).invoke().method("postValue").withArgs(value)
    doWait(1000)
    return value
}

@Suppress("UNUSED_PARAMETER")
private fun <T> typedResponseLiveData(aClass: Class<T>): ResponseLiveData<T> {
    return object : ResponseLiveData<T>() {
        override fun compute() = Unit
    }
}

private fun getRawType(type: Type): Class<*> = when (type) {
    is Class<*> -> type
    is ParameterizedType -> type.rawType as? Class<*> ?: throw IllegalArgumentException()
    is GenericArrayType -> {
        val componentType = type.genericComponentType
        Array.newInstance(getRawType(componentType), 0).javaClass
    }
    is TypeVariable<*> -> Any::class.java
    is WildcardType -> getRawType(type.upperBounds[0])
    else -> throw IllegalArgumentException("Expected a Class, ParameterizedType, or GenericArrayType, but <$type> is of type ${type.javaClass.name}")
}
