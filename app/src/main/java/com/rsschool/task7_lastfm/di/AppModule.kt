package com.rsschool.task7_lastfm.di

import com.rsschool.task7_lastfm.BASE_URL
import com.rsschool.task7_lastfm.model.api.ArtistsApiImpl
import com.rsschool.task7_lastfm.model.api.LastFmApi
import com.rsschool.task7_lastfm.repository.IRepository
import com.rsschool.task7_lastfm.repository.Repository
import com.rsschool.task7_lastfm.ui.ArtistsViewControlsState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRepository(artistsApiImpl: ArtistsApiImpl): IRepository = Repository(artistsApiImpl)

    @Singleton
    @Provides
    fun provideArtistsApiImpl(api: LastFmApi):ArtistsApiImpl = ArtistsApiImpl(api)

    @Singleton
    @Provides
    fun provideArtistsViewControlsState(): ArtistsViewControlsState = ArtistsViewControlsState()

    @Singleton
    @Provides
    fun provideRetrofit(moshiConverter: MoshiConverterFactory): Retrofit = Retrofit.Builder()
        .addConverterFactory(moshiConverter)
        .baseUrl(BASE_URL)
        .build()

    @Singleton
    @Provides
    fun provideMoshiConverter(): MoshiConverterFactory = MoshiConverterFactory.create()

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): LastFmApi =  retrofit.create(LastFmApi::class.java)

}