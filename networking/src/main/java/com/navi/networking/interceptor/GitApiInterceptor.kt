package com.navi.networking.interceptor

import com.navi.utils.Constants.HEADER_AUTH_TOKEN
import dagger.Reusable
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

@Reusable
internal class GitApiInterceptor @Inject constructor() : Interceptor {
    companion object {
        private const val HEADER_AUTH_KEY = "Authorization"

        private const val HEADER_ACCEPT_KEY = "Accept"
        private const val HEADER_ACCEPT_TYPE = "application/vnd.github+json"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(
            chain.request().newBuilder()
                .addHeader(HEADER_AUTH_KEY, HEADER_AUTH_TOKEN)
                .addHeader(HEADER_ACCEPT_KEY, HEADER_ACCEPT_TYPE)
                .build()
        )
    }
}