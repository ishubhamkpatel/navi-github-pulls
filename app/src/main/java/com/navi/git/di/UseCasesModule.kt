package com.navi.git.di

import com.navi.git.features.error.ErrorUseCase
import com.navi.git.features.error.ErrorUseCaseImpl
import com.navi.git.features.pullrequests.PullRequestsUseCase
import com.navi.git.features.pullrequests.PullRequestsUseCaseImpl
import com.navi.git.features.userrepoinput.UserRepoInputUseCase
import com.navi.git.features.userrepoinput.UserRepoInputUseCaseImpl
import com.navi.git.main.MainUseCase
import com.navi.git.main.MainUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal abstract class UseCasesModule {
    @Binds
    abstract fun bindMainUseCase(mainUseCaseImpl: MainUseCaseImpl): MainUseCase

    @Binds
    abstract fun bindUserRepoInputUseCase(userRepoInputUseCaseImpl: UserRepoInputUseCaseImpl): UserRepoInputUseCase

    @Binds
    abstract fun bindPullRequestsUseCase(pullRequestsUseCaseImpl: PullRequestsUseCaseImpl): PullRequestsUseCase

    @Binds
    abstract fun bindErrorUseCase(errorUseCaseImpl: ErrorUseCaseImpl): ErrorUseCase
}