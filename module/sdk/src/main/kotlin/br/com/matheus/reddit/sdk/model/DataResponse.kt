package br.com.matheus.reddit.sdk.model

import br.com.matheus.reddit.sdk.model.type.DataResultStatus

data class DataResponse<out T>(
        val data: T?,
        var error: Throwable?,
        @get:DataResultStatus
        @DataResultStatus val resultStatus: Long
)