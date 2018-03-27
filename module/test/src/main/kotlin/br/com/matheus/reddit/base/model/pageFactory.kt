package br.com.matheus.reddit.base.model

import br.com.matheus.reddit.sdk.model.Page

fun <T> emptyPage(): Page<T> {
    return Page("", "", 0, "", "", emptyList())
}

fun <T> pageWith(vararg data: T, after: String = ""): Page<T> {
    return Page(after, "", 0, "", "", data.asList())
}