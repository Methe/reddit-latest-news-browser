package br.com.matheus.reddit.sdk

import android.os.Handler
import android.os.Looper

internal fun async(block: () -> Unit) {
    Thread(block).start()
}

internal fun mainThread(block: () -> Unit) {
    Handler(Looper.getMainLooper()).post(block)
}