package br.com.matheus.reddit.sdk

import android.app.Application
import br.com.matheus.reddit.sdk.prng.PrngFixes
import com.jakewharton.threetenabp.AndroidThreeTen
import timber.log.Timber

fun setupApplication(application: Application) {
    AndroidThreeTen.init(application)
    PrngFixes.apply()
    if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
}
