package br.com.matheus.reddit.list

import android.support.test.rule.ActivityTestRule
import br.com.concretesolutions.kappuccino.actions.ClickActions.click
import br.com.concretesolutions.kappuccino.assertions.VisibilityAssertions.displayed
import br.com.concretesolutions.kappuccino.assertions.VisibilityAssertions.notDisplayed
import br.com.concretesolutions.kappuccino.custom.intent.IntentMatcherInteractions.matchIntent
import br.com.concretesolutions.kappuccino.custom.recyclerView.RecyclerViewInteractions.recyclerView
import br.com.matheus.reddit.R
import br.com.matheus.reddit.base.BaseActivityRobot
import br.com.matheus.reddit.base.extension.mockCreation
import br.com.matheus.reddit.base.extension.mockError
import br.com.matheus.reddit.base.extension.mockLoading
import br.com.matheus.reddit.base.extension.mockValue
import br.com.matheus.reddit.base.model.emptyPage
import br.com.matheus.reddit.base.model.postPage
import br.com.matheus.reddit.base.model.postPageWithNext
import br.com.matheus.reddit.detail.PostActivity
import br.com.matheus.reddit.sdk.PostRepository

fun PostListTest.postList(func: PostListRobot.() -> Unit): PostListRobot {
    return PostListRobot(rule).apply(func)
}

class PostListRobot(rule: ActivityTestRule<PostListActivity>)
    : BaseActivityRobot<PostListActivity>(rule) {

    private val liveData = PostRepository.firstPostPage.mockCreation()
    private val nextPage = PostRepository.getNextPostPage("after").mockCreation()

    fun onErrorState() {
        launchActivity()
        receiveError {
            errorStateDisplayed()
        }
    }

    fun onSuccessState() {
        launchActivity()
        receiveList {
            successStateDisplayed()
        }
    }

    fun withNextPage() {
        launchActivity()
        liveData.mockValue(postPageWithNext())
        successStateDisplayed()
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

    fun postDetailIsDisplayed() {
        matchIntent {
            className(PostActivity::class.java.name)
        }
    }

    fun nextPageRequested() {
        nextPage.mockValue(postPage())
    }

    fun nextPageErrorDisplayed() {
        displayed { parent(R.id.item_progress) { id(R.id.error) } }
    }

    infix fun receiveLoading(func: PostListRobot.() -> Unit) {
        liveData.mockLoading()
        apply(func)
    }

    infix fun receiveError(func: PostListRobot.() -> Unit) {
        liveData.mockError(IllegalStateException("Mocked Error"))
        apply(func)
    }

    infix fun receiveEmpty(func: PostListRobot.() -> Unit) {
        liveData.mockValue(emptyPage())
        apply(func)
    }

    infix fun receiveList(func: PostListRobot.() -> Unit) {
        liveData.mockValue(postPage())
        apply(func)
    }

    infix fun scrollUntilLoadIsDisplayed(func: PostListRobot.() -> Unit) {
        recyclerView(R.id.recycler_view) {
            atPosition(6) { scroll() }
        }
        apply(func)
    }

    infix fun nextPageFails(func: PostListRobot.() -> Unit): PostListRobot {
        scrollUntilLoadIsDisplayed { }
        nextPage.mockError(IllegalStateException("Mocked Error"))
        return apply(func)
    }

    infix fun clickOnNextPageError(func: PostListRobot.() -> Unit) {
        click { parent(R.id.item_progress) { id(R.id.error) } }
        nextPage.mockValue(postPage())
        apply(func)
    }

    infix fun clickOnRetry(func: PostListRobot.() -> Unit) {
        click {
            parent(R.id.error_view) { id(R.id.action_view) }
        }
        liveData.mockValue(emptyPage())
        apply(func)
    }

    infix fun clickOnItem(func: PostListRobot.() -> Unit) {

        matchIntent {
            className(PostActivity::class.java.name)
            result { ok() }
        }

        recyclerView(R.id.recycler_view) {
            atPosition(0) { click() }
        }
        apply(func)
    }

}