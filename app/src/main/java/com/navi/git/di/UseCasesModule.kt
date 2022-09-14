package com.navi.git.di

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
}