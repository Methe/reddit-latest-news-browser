package br.com.matheus.reddit.sdk

import br.com.matheus.reddit.sdk.data.remote.apiInstance
import br.com.matheus.reddit.sdk.extension.extractResult
import br.com.matheus.reddit.sdk.extension.extractResultFromPage
import br.com.matheus.reddit.sdk.extension.map

object CommentRepository {

    fun getFirstCommentPage(postId: String) = getNextCommentPage(postId, "")

    fun getNextCommentPage(postId: String, page: String) = apiInstance.listCommentFromPost(postId = postId, afterPage = page)
            .map { it.last() }
            .extractResult()
            .map {
                it.copy(children = it.children.filter { it.kind != "more" })
            }
            .extractResultFromPage()

}