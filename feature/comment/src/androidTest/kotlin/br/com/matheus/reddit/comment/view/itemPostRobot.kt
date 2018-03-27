package br.com.matheus.reddit.comment.view

import android.support.test.rule.ActivityTestRule
import br.com.concretesolutions.kappuccino.assertions.VisibilityAssertions.displayed
import br.com.matheus.reddit.TestActivity
import br.com.matheus.reddit.base.BaseViewRobot
import br.com.matheus.reddit.base.model.randomComment
import br.com.matheus.reddit.comment.R

fun ItemCommentTest.itemComment(func: PostListRobot.() -> Unit): PostListRobot {
    return PostListRobot(rule).apply {
        val view = ItemCommentView(rule.activity)
        launchView(view)
        view.bind(model)
        func()
    }
}

class PostListRobot(rule: ActivityTestRule<TestActivity>) : BaseViewRobot(rule) {

    val model = randomComment()

    fun authorIsDisplayed() {
        displayed {
            allOf {
                id(R.id.comment_author)
                text(model.author)
            }
        }
    }

    fun bodyIsDisplayed() {
        displayed {
            allOf {
                id(R.id.comment_body)
                text(model.body)
            }
        }
    }

    fun upsIsDisplayed() {
        displayed {
            allOf {
                id(R.id.comment_ups)
                text("${model.ups}")
            }
        }
    }

    fun downsIsDisplayed() {
        displayed {
            allOf {
                id(R.id.comment_downs)
                text("${model.downs}")
            }
        }
    }

}