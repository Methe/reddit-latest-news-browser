package br.com.matheus.reddit

import android.app.Application
import br.com.matheus.reddit.sdk.setupApplication

class RedditApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        setupApplication(this)
    }
}
