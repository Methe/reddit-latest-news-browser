package br.com.matheus.reddit.base

import android.content.Intent
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.v7.app.AppCompatActivity
import br.com.matheus.reddit.TestActivity
import br.com.matheus.reddit.base.extension.mockRepository
import br.com.matheus.reddit.sdk.CommentRepository
import br.com.matheus.reddit.sdk.PostRepository
import org.junit.Before
import org.junit.Rule
import kotlin.reflect.KClass

open class BaseActivityTest<AC : AppCompatActivity>(activityClass: KClass<AC>) {

    @Rule
    @JvmField
    val rule = IntentsTestRule(activityClass.java, true, false)

    @Before
    fun setup() {
        mockRepository(PostRepository::class.java)
        mockRepository(CommentRepository::class.java)
    }

}

open class BaseFragmentTest : BaseActivityTest<TestActivity>(TestActivity::class) {
    @Before
    fun launchActivity() {
        rule.launchActivity(Intent())
    }
}

open class BaseViewTest : BaseActivityTest<TestActivity>(TestActivity::class) {
    @Before
    fun launchActivity() {
        rule.launchActivity(Intent())
    }
}