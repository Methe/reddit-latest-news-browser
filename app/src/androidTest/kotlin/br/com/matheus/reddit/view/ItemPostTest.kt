package br.com.matheus.reddit.view

import android.support.test.runner.AndroidJUnit4
import br.com.matheus.reddit.base.BaseViewTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ItemPostTest : BaseViewTest() {

    @Test
    fun shouldDisplayPostInfo() {
        itemPost {
            titleIsDisplayed()
            authorIsDisplayed()
            commentsIsDisplayed()
            upsIsDisplayed()
            downsIsDisplayed()
        }
    }

}
