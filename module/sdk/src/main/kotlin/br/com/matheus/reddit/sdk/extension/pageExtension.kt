package br.com.matheus.reddit.sdk.extension

import br.com.matheus.reddit.sdk.model.Page
import br.com.matheus.reddit.sdk.model.Result

internal fun <T> Page<Result<T>>.extractResult(): Page<T> = map(Result<T>::data)

fun <T, R> Page<T>.map(transformation: (T) -> R) =
        Page(after, before, dist, modhash, whitelistStatus, children.map(transformation))

operator fun <T> Page<T>?.plus(nextPage: Page<T>) = Page(
        after = nextPage.after,
        before = nextPage.before,
        dist = nextPage.dist,
        modhash = nextPage.modhash,
        whitelistStatus = nextPage.whitelistStatus,
        children = (this?.children ?: emptyList()) + nextPage.children
)