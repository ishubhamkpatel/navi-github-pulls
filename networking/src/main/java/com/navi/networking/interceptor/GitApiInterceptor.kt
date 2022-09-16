package com.navi.networking.interceptor

import com.navi.secrets.Secrets
import dagger.Reusable
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

@Reusable
internal class GitApiInterceptor @Inject constructor(private val secrets: Secrets) : Interceptor {
    companion object {
        private const val HEADER_AUTH_KEY = "Authorization"
        private const val HEADER_AUTH_TOKEN_TYPE = "Bearer"

        private const val HEADER_ACCEPT_KEY = "Accept"
        private const val HEADER_ACCEPT_TYPE = "application/vnd.github+json"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(
            chain.request().newBuilder()
                .addHeader(HEADER_AUTH_KEY, "$HEADER_AUTH_TOKEN_TYPE ${secrets.getAccessToken()}")
                .addHeader(HEADER_ACCEPT_KEY, HEADER_ACCEPT_TYPE)
                .build()
        )
    }
}