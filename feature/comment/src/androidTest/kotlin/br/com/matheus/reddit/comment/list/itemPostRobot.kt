package br.com.matheus.reddit.comment.list

import android.support.test.rule.ActivityTestRule
import br.com.concretesolutions.kappuccino.actions.ClickActions.click
import br.com.concretesolutions.kappuccino.assertions.VisibilityAssertions.displayed
import br.com.concretesolutions.kappuccino.assertions.VisibilityAssertions.notDisplayed
import br.com.matheus.reddit.TestActivity
import br.com.matheus.reddit.base.BaseFragmentRobot
import br.com.matheus.reddit.base.extension.mockCreation
import br.com.matheus.reddit.base.extension.mockError
import br.com.matheus.reddit.base.extension.mockLoading
import br.com.matheus.reddit.base.extension.mockValue
import br.com.matheus.reddit.base.model.commentPage
import br.com.matheus.reddit.base.model.emptyPage
import br.com.matheus.reddit.comment.R
import br.com.matheus.reddit.sdk.CommentRepository

fun CommentListTest.commentList(func: CommentListRobot.() -> Unit): CommentListRobot {
    return CommentListRobot(rule).apply {
        launchFragment(CommentListFragment.instance(""))
        func()
    }
}

class CommentListRobot(rule: ActivityTestRule<TestActivity>) : BaseFragmentRobot(rule) {

    private val liveData = CommentRepository.getFirstCommentPage("").mockCreation()

    fun onErrorState() {
        receiveError {
            errorStateDisplayed()
        }
    }

    fun loadingStateDisplayed() {
        displayed { id(R.id.progress_view) }
        notDisplayed {
            id(R.id.empty_view)
            id(R.id.error_view)
            id(R.id.recycler_view)
        }
    }

    fun errorStateDisplayed() {
        displayed { id(R.id.error_view) }
        notDisplayed {
            id(R.id.empty_view)
            id(R.id.recycler_view)
            id(R.id.progress_view)
        }
    }

    fun emptyStateDisplayed() {
        displayed { id(R.id.empty_view) }
        notDisplayed {
            id(R.id.recycler_view)
            id(R.id.error_view)
            id(R.id.progress_view)
        }
    }

    fun successStateDisplayed() {
        displayed { id(R.id.recycler_view) }
        notDisplayed {
            id(R.id.empty_view)
            id(R.id.error_view)
            id(R.id.progress_view)
        }
    }

    fun dataHasBeenInvalidated() = emptyStateDisplayed()

    infix fun receiveLoading(func: CommentListRobot.() -> Unit) {
        liveData.mockLoading()
        apply(func)
    }

    infix fun receiveError(func: CommentListRobot.() -> Unit) {
        liveData.mockError(IllegalStateException("Mocked Error"))
        apply(func)
    }

    infix fun receiveEmpty(func: CommentListRobot.() -> Unit) {
        liveData.mockValue(emptyPage())
        apply(func)
    }

    infix fun receiveList(func: CommentListRobot.() -> Unit) {
        liveData.mockValue(commentPage())
        apply(func)
    }

    infix fun clickOnRetry(func: CommentListRobot.() -> Unit) {
        click {
            parent(R.id.error_view) { id(R.id.action_view) }
        }
        liveData.mockValue(emptyPage())
        apply(func)
    }

}