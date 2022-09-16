package com.navi.networking.di

import com.navi.networking.Api
import com.navi.networking.ApiManager
import com.navi.networking.ApiManagerImpl
import com.navi.networking.interceptor.GitApiInterceptor
import com.navi.networking.repo.GitRepository
import com.navi.networking.repo.GitRepositoryImpl
import com.navi.secrets.Secrets
import com.navi.utils.moshi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class Module {
    @Binds
    abstract fun bindGitRepository(gitRepositoryImpl: GitRepositoryImpl): GitRepository

    @Binds
    abstract fun bindApiManager(apiManagerImpl: ApiManagerImpl): ApiManager
}

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {
    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): Api = retrofit.create(Api::class.java)

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        moshiConverterFactory: Converter.Factory
    ): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .client(okHttpClient)
        .addConverterFactory(moshiConverterFactory)
        .build()

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @OkHttpLoggingInterceptor httpLoggingInterceptor: Interceptor,
        @GitHubApiInterceptor gitApiInterceptor: Interceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .apply {
                readTimeout(10, TimeUnit.SECONDS)
                writeTimeout(10, TimeUnit.SECONDS)
                callTimeout(10, TimeUnit.SECONDS)
            }
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(gitApiInterceptor)
            .build()

    @Provides
    @Reusable
    fun provideMoshiConverterFactory(): Converter.Factory = MoshiConverterFactory.create(moshi)

    @Provides
    @Reusable
    @OkHttpLoggingInterceptor
    fun provideHttpLoggingInterceptor(): Interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    @Reusable
    @GitHubApiInterceptor
    fun provideGitApiInterceptor(secrets: Secrets): Interceptor = GitApiInterceptor(secrets)
}