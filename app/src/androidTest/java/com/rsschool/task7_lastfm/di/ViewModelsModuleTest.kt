package com.rsschool.task7_lastfm.di

import com.rsschool.task7_lastfm.repository.FakeRepository
import com.rsschool.task7_lastfm.ui.viewmodels.AlbumsViewModel
import com.rsschool.task7_lastfm.ui.viewmodels.ArtistsViewModel
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
    fun provideArtistsViewModel(repository: FakeRepository): ArtistsViewModel =
        ArtistsViewModel(repository)

    @Singleton
    @Provides
    fun provideModelsViewModel(repository: FakeRepository): AlbumsViewModel =
        AlbumsViewModel(repository)

    @Singleton
    @Provides
    fun provideFakeRepository(): FakeRepository = FakeRepository()
}