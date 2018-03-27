package br.com.matheus.reddit.sdk

import br.com.matheus.reddit.sdk.data.remote.apiInstance
import br.com.matheus.reddit.sdk.extension.extractResult
import br.com.matheus.reddit.sdk.extension.extractResultFromPage
import br.com.matheus.reddit.sdk.extension.onNext
import br.com.matheus.reddit.sdk.extension.plus

object PostRepository {

    val firstPostPage = getFormattedPostPage("")

    fun getNextPostPage(page: String) = getFormattedPostPage(page)
            .onNext {
                firstPostPage.updateValue(firstPostPage.getData() + it)
            }

    private fun getFormattedPostPage(page: String) = apiInstance.listProjects(afterPage = page)
            .extractResult()
            .extractResultFromPage()

}