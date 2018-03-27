package br.com.matheus.reddit.comment.list

import br.com.matheus.reddit.base.BaseViewModel
import br.com.matheus.reddit.sdk.CommentRepository
import br.com.matheus.reddit.sdk.liveData.ResponseLiveData
import br.com.matheus.reddit.sdk.model.Page
import br.com.matheus.reddit.sdk.model.domain.CommentVO

class CommentListViewModel : BaseViewModel() {

    private var postId: String? = null
    private var firstPageLiveData: ResponseLiveData<Page<CommentVO>>? = null

    fun getCommentPageLiveData(postId: String): ResponseLiveData<Page<CommentVO>> {
        firstPageLiveData = if (postId == this.postId) firstPageLiveData
        else CommentRepository.getFirstCommentPage(postId)

        this.postId = postId

        return firstPageLiveData!!
    }

}