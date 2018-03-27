package br.com.matheus.reddit.list

import android.support.test.runner.AndroidJUnit4
import br.com.matheus.reddit.base.BaseActivityTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PostListTest : BaseActivityTest<PostListActivity>(PostListActivity::class) {

    @Test
    fun whenReceiveLoading_shouldDisplayLoadingState() {
        postList {
            launchActivity()
        } receiveLoading {
            loadingStateDisplayed()
        }
    }

    @Test
    fun whenReceiveError_shouldDisplayErrorState() {
        postList {
            launchActivity()
        } receiveError {
            errorStateDisplayed()
        }
    }

    @Test
    fun whenReceiveEmpty_shouldDisplayEmptyState() {
        postList {
            launchActivity()
        } receiveEmpty {
            emptyStateDisplayed()
        }
    }

    @Test
    fun whenReceiveList_shouldDisplaySuccessState() {
        postList {
            launchActivity()
        } receiveList {
            successStateDisplayed()
        }
    }

    @Test
    fun onErrorState_clickOnRetry_shouldInvalidateData() {
        postList {
            onErrorState()
        } clickOnRetry {
            dataHasBeenInvalidated()
        }
    }

    @Test
    fun onSuccessState_clickOnItem_shouldOpenPostDetail() {
        postList {
            onSuccessState()
        } clickOnItem {
            postDetailIsDisplayed()
        }
    }

    @Test
    fun whenHasNextPage_shouldRequestAnotherPage() {
        postList {
            withNextPage()
        } scrollUntilLoadIsDisplayed {
            nextPageRequested()
        }
    }

    @Test
    fun whenNextPageFails_shouldDisplayErrorState_clickOnError_shouldRetry() {
        postList {
            withNextPage()
        } nextPageFails {
            nextPageErrorDisplayed()
        } clickOnNextPageError {
            nextPageRequested()
        }
    }

}
