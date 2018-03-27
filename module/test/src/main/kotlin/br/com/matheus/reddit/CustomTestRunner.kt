package br.com.matheus.reddit

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.support.test.runner.AndroidJUnitRunner
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import android.widget.ProgressBar
import com.github.tmurakami.dexopener.DexOpener
import net.vidageek.mirror.dsl.Mirror

class CustomTestRunner : AndroidJUnitRunner() {

    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
        // Not proud =(
        val aClass = try {
            Class.forName("${context.packageName}.BuildConfig")
        } catch (error: ClassNotFoundException) {
            Class.forName("${className.substringBeforeLast(".")}.BuildConfig")
        }

        val buildConfig = DexOpener.builder(context)
                .buildConfig(aClass)
        Mirror().on(buildConfig).set().field("packageToBeOpened").withValue("br.com.matheus.reddit")
        buildConfig.build().installTo(cl)
        return super.newApplication(cl, className, context).apply { applyRuleToRemoveAllProgressBars(this) }
    }

}

// Roubei ...
private fun applyRuleToRemoveAllProgressBars(application: Application) {
    application.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
        override fun onActivityResumed(activity: Activity) {
            getAllProgressBars(activity.findViewById(android.R.id.content)).forEach {
                it.indeterminateDrawable = activity.getDrawable(android.R.drawable.ic_menu_revert)
            }
        }

        override fun onActivityPaused(activity: Activity) = Unit
        override fun onActivityStarted(activity: Activity) = Unit
        override fun onActivityDestroyed(activity: Activity) = Unit
        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit
        override fun onActivityStopped(activity: Activity) = Unit
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            (activity as AppCompatActivity).supportFragmentManager.registerFragmentLifecycleCallbacks(object : FragmentManager.FragmentLifecycleCallbacks() {
                override fun onFragmentActivityCreated(fm: FragmentManager?, f: Fragment?, savedInstanceState: Bundle?) {
                    super.onFragmentActivityCreated(fm, f, savedInstanceState)
                    getAllProgressBars(activity.findViewById(android.R.id.content)).forEach {
                        it.indeterminateDrawable = activity.getDrawable(android.R.drawable.ic_menu_revert)
                    }
                }
            }, true)
        }

        private fun getAllProgressBars(root: ViewGroup): ArrayList<ProgressBar> {
            val views = ArrayList<ProgressBar>()
            val childCount = root.childCount
            (0 until childCount)
                    .map { root.getChildAt(it) }
                    .forEach {
                        if (it is ViewGroup)
                            views.addAll(getAllProgressBars(it))
                        if (it is ProgressBar)
                            views.add(it)
                    }
            return views
        }
    })
}