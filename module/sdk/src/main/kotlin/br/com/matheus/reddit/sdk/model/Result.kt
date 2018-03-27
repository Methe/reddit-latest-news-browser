package br.com.matheus.reddit.sdk.model

import com.google.gson.annotations.Expose

data class Result<out T>(@Expose val kind: String,
                                  @Expose val data: T)