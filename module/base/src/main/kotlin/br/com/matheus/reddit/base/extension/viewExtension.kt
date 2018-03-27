package br.com.matheus.reddit.base.extension

import android.content.res.TypedArray
import android.os.Build
import android.provider.Settings
import android.support.annotation.DrawableRes
import android.support.annotation.StyleRes
import android.support.v7.app.ActionBar
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation

val View.isAnimationEnabled: Boolean
    get() {
        val duration: Float = Settings.Global.getFloat(
                context.contentResolver,
                Settings.Global.ANIMATOR_DURATION_SCALE, 1f)
        val transition: Float = Settings.Global.getFloat(
                context.contentResolver,
                Settings.Global.TRANSITION_ANIMATION_SCALE, 1f)

        return duration != 0f && transition != 0f
    }

fun View?.addStatusBarPadding() {
    if(this == null) return
    setPadding(paddingLeft,
            paddingTop + context.statusBarHeight(), paddingRight,
            paddingBottom)
}

fun View?.addStatusBarMargin() {
    if(this == null) return
    val params = layoutParams as ViewGroup.MarginLayoutParams
    params.topMargin += context.statusBarHeight()
}

fun ActionBar?.enableBack() {
    if (this == null) return
    setDisplayHomeAsUpEnabled(true)
    setDisplayShowHomeEnabled(true)
}

fun ImageView.loadUrl(url: String?, @DrawableRes placeholder: Int = 0, @DrawableRes error: Int = placeholder, transformation: Transformation? = null) {
    if (url.isNullOrEmpty()) {
        if (error > 0) setImageResource(error)
        else if (placeholder > 0) setImageResource(placeholder)
        return
    }
    Picasso.with(context).load(url).apply {
        transformation?.let(::transform)
        if (placeholder > 0) placeholder(placeholder)
        if (error > 0) error(error)
    }.into(this)
}

@Suppress("DEPRECATION")
fun TextView.setCustomTextAppearance(@StyleRes styleRes: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) setTextAppearance(styleRes)
    else setTextAppearance(context, styleRes)
}

inline fun TypedArray.obtain(func: TypedArray.() -> Unit) {
    func()
    recycle()
}