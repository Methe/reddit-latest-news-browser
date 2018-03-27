package br.com.matheus.reddit.detail

import android.content.Intent
import android.support.test.rule.ActivityTestRule
import br.com.concretesolutions.kappuccino.assertions.VisibilityAssertions.displayed
import br.com.matheus.reddit.R
import br.com.matheus.reddit.base.BaseActivityRobot
import br.com.matheus.reddit.base.model.randomPost
import br.com.matheus.reddit.sdk.model.domain.PostVO

fun PostTest.post(func: PostRobot.() -> Unit): PostRobot {
    return PostRobot(rule, randomPost()).apply {
        launchActivity(Intent().putExtra("EXTRA_POST", post))
        func()
    }
}

class PostRobot(rule: ActivityTestRule<PostActivity>, val post: PostVO)
    : BaseActivityRobot<PostActivity>(rule) {

    fun titleOnToolbarIsDisplayed() {
        displayed {
            parent(R.id.toolbar) {
                text(post.title)
            }
        }
    }

    fun infoIsDisplayed() {
        displayed {
            id(R.id.post_info)
        }
    }

    fun contentIsDisplayed() {
        displayed {
            allOf {
                id(R.id.post_content)
                text(post.selftext)
            }
        }
    }

}