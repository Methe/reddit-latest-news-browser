package br.com.matheus.reddit.comment.view

import android.support.test.runner.AndroidJUnit4
import br.com.matheus.reddit.base.BaseViewTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ItemCommentTest : BaseViewTest() {

    @Test
    fun shouldDisplayCommentInfo() {
        itemComment {
            authorIsDisplayed()
            bodyIsDisplayed()
            upsIsDisplayed()
            downsIsDisplayed()
        }
    }

}
