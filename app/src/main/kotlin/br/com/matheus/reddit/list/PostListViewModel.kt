package br.com.matheus.reddit.list

import br.com.matheus.reddit.base.BaseViewModel
import br.com.matheus.reddit.sdk.PostRepository
import br.com.matheus.reddit.sdk.liveData.ResponseLiveData
import br.com.matheus.reddit.sdk.model.Page
import br.com.matheus.reddit.sdk.model.domain.PostVO

class PostListViewModel : BaseViewModel() {

    val postListLiveData = PostRepository.firstPostPage

    private var lastRequestedPage: Pair<String, ResponseLiveData<Page<PostVO>>>? = null

    fun getNextPostPageLiveData(next: String): ResponseLiveData<Page<PostVO>> {
        val nextPageLiveData = lastRequestedPage?.takeIf { it.first == next }?.second
                ?: PostRepository.getNextPostPage(next)

        lastRequestedPage = next to nextPageLiveData
        return nextPageLiveData
    }

}
