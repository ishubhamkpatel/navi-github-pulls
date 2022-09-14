package com.navi.networking.di

import javax.inject.Qualifier

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
internal annotation class OkHttpLoggingInterceptor

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
internal annotation class GitHubApiInterceptor