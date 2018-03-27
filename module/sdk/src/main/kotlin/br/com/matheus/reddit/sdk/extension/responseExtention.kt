@file:JvmName("ResponseUtils")

package br.com.matheus.reddit.sdk.extension

import br.com.matheus.reddit.sdk.model.DataResponse
import br.com.matheus.reddit.sdk.model.type.DataResultStatus
import br.com.matheus.reddit.sdk.model.type.ERROR
import br.com.matheus.reddit.sdk.model.type.LOADING

internal fun loadingResponse() = DataResponse(null, null, LOADING)

internal fun <T> T?.toDataResponse(@DataResultStatus status: Long) = DataResponse(this, null, status)

internal fun <T> T?.toDataResponseWithError(error: Throwable) = DataResponse(this, error, ERROR)

internal fun <T> Throwable.toErrorResponse() = DataResponse<T>(null, this, ERROR)