package br.com.matheus.reddit.base

import android.content.Intent
import android.support.test.rule.ActivityTestRule
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import br.com.concretesolutions.kappuccino.utils.doWait
import br.com.matheus.reddit.TestActivity

open class BaseActivityRobot<T : AppCompatActivity>(val rule: ActivityTestRule<T>) {

    fun launchActivity(intent: Intent = Intent()) {
        rule.launchActivity(intent)
        doWait(200L)
    }

}

open class BaseFragmentRobot(rule: ActivityTestRule<TestActivity>) : BaseActivityRobot<TestActivity>(rule) {

    fun launchFragment(fragment: Fragment) {
        rule.activity?.setFragment(fragment)
        doWait(200L)
    }

}

open class BaseViewRobot(rule: ActivityTestRule<TestActivity>) : BaseActivityRobot<TestActivity>(rule) {

    fun launchView(view: View) {
        rule.activity?.setView(view)
    }

}