package br.com.matheus.reddit.comment.list

import android.support.test.runner.AndroidJUnit4
import br.com.matheus.reddit.base.BaseFragmentTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CommentListTest : BaseFragmentTest() {

    @Test
    fun whenReceiveLoading_shouldDisplayLoadingState() {
        commentList {
        } receiveLoading {
            loadingStateDisplayed()
        }
    }

    @Test
    fun whenReceiveError_shouldDisplayErrorState() {
        commentList {
        } receiveError {
            errorStateDisplayed()
        }
    }

    @Test
    fun whenReceiveEmpty_shouldDisplayEmptyState() {
        commentList {
        } receiveEmpty {
            emptyStateDisplayed()
        }
    }

    @Test
    fun whenReceiveList_shouldDisplaySuccessState() {
        commentList {
        } receiveList {
            successStateDisplayed()
        }
    }

    @Test
    fun onErrorState_clickOnRetry_shouldInvalidateData() {
        commentList {
            onErrorState()
        } clickOnRetry {
            dataHasBeenInvalidated()
        }
    }
}
