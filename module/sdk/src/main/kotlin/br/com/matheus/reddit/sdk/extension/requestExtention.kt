package br.com.matheus.reddit.sdk.extension

import retrofit2.Call

@Throws(Exception::class)
internal fun <T> Call<T>.makeRequest() = if (isExecuted) clone().execute().body()!! else execute().body()!!