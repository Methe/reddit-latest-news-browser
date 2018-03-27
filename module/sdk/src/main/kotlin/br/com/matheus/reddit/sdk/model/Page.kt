package br.com.matheus.reddit.sdk.model

import com.google.gson.annotations.Expose

data class Page<out T>(
        @Expose val after: String?,
        @Expose val before: String?,
        @Expose val dist: Int,
        @Expose val modhash: String,
        @Expose val whitelistStatus: String?,
        @Expose val children: List<T>
) {
    fun isNotEmpty() = children.isNotEmpty()

    fun isEmpty() = children.isEmpty()
}