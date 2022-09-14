package com.navi.logger

import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean

object Logger {
    private val isDebugTreePlanted = AtomicBoolean(false)

    fun init(isDebuggable: Boolean) {
        if (isDebuggable and isDebugTreePlanted.compareAndSet(false, true)) {
            Timber.plant(Timber.DebugTree())
        }
    }

    fun d(message: String) {
        Timber.d(message)
    }

    fun d(throwable: Throwable) {
        Timber.d(throwable)
    }

    fun e(message: String) {
        Timber.e(message)
    }

    fun e(throwable: Throwable) {
        Timber.e(throwable)
    }
}