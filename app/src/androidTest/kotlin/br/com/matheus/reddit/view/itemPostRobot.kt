package br.com.matheus.reddit.view

import android.support.test.rule.ActivityTestRule
import br.com.concretesolutions.kappuccino.assertions.VisibilityAssertions.displayed
import br.com.matheus.reddit.R
import br.com.matheus.reddit.TestActivity
import br.com.matheus.reddit.base.BaseViewRobot
import br.com.matheus.reddit.base.model.randomPost

fun ItemPostTest.itemPost(func: PostListRobot.() -> Unit): PostListRobot {
    return PostListRobot(rule).apply {
        val view = ItemPostView(rule.activity)
        launchView(view)
        view.bind(model)
        func()
    }
}

class PostListRobot(rule: ActivityTestRule<TestActivity>) : BaseViewRobot(rule) {

    val model = randomPost()

    fun titleIsDisplayed() {
        displayed {
            allOf {
                id(R.id.post_title)
                text(model.title)
            }
        }
    }

    fun authorIsDisplayed() {
        displayed {
            allOf {
                id(R.id.post_author)
                text(model.author)
            }
        }
    }

    fun commentsIsDisplayed() {
        displayed {
            allOf {
                id(R.id.post_comments)
                text("${model.numComments}")
            }
        }
    }

    fun upsIsDisplayed() {
        displayed {
            allOf {
                id(R.id.post_ups)
                text("${model.ups}")
            }
        }
    }

    fun downsIsDisplayed() {
        displayed {
            allOf {
                id(R.id.post_downs)
                text("${model.downs}")
            }
        }
    }

}