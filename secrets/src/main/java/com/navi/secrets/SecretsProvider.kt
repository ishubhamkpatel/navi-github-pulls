package com.navi.secrets

import android.content.Context
import android.os.Build
import com.getkeepsafe.relinker.ReLinker
import com.navi.logger.Logger
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Secrets @Inject constructor(@ApplicationContext context: Context) {
    companion object {
        private const val LIBRARY_NAME = "secrets"
    }

    private val nativeLib by lazy { NativeLib() }

    init {
        kotlin.runCatching {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                System.loadLibrary(LIBRARY_NAME)
            } else {
                ReLinker.loadLibrary(context, LIBRARY_NAME, object : ReLinker.LoadListener {
                    override fun success() {
                        Logger.d("Native-lib loaded successfully by ReLinker.")
                    }

                    override fun failure(t: Throwable?) {
                        Logger.e("Native-lib load failed by ReLinker; error message = ${t?.message}")
                    }
                })
            }
        }.getOrElse {
            Logger.e("Native-lib load failed; error message = ${it.message}")
        }
    }

    fun getAccessToken() = nativeLib.githubAccessToken()
}