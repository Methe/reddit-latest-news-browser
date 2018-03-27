package br.com.matheus.reddit

import android.app.Application
import br.com.matheus.reddit.sdk.setupSdk

class RedditApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        setupSdk()
    }
}