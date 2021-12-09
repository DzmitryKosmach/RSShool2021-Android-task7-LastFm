package com.rsschool.task7_lastfm.di

import com.rsschool.task7_lastfm.repository.FakeRepository
import com.rsschool.task7_lastfm.ui.viewmodels.TracksViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object ViewModelsModule {
    @Singleton
    @Provides
    fun provideTracksViewModel(repository: FakeRepository): TracksViewModel =
        TracksViewModel(repository)

    @Singleton
    @Provides
    fun provideFakeRepository(): FakeRepository = FakeRepository()
}