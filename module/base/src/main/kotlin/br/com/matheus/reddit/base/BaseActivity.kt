package br.com.matheus.reddit.base

import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import br.com.matheus.reddit.base.extension.toast

abstract class BaseActivity : AppCompatActivity() {

    open fun onErrorReceived(throwable: Throwable) {
        toast(throwable)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onHomeClick()
        return super.onOptionsItemSelected(item)
    }

    open fun onHomeClick() = finish()

}