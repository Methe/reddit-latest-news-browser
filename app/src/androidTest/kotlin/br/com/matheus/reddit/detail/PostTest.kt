package br.com.matheus.reddit.detail

import android.support.test.runner.AndroidJUnit4
import br.com.matheus.reddit.base.BaseActivityTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PostTest : BaseActivityTest<PostActivity>(PostActivity::class) {

    @Test
    fun whenEnter_shouldDisplayPostInfo() {
        post {
            titleOnToolbarIsDisplayed()
            infoIsDisplayed()
            contentIsDisplayed()
        }
    }

}
