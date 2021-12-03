package com.rsschool.task7_lastfm.repository

import com.rsschool.task7_lastfm.model.api.ArtistsApiImpl
import javax.inject.Inject

class Repository @Inject constructor(
    private val api: ArtistsApiImpl
) {

    suspend fun getTopArtists(page: Int, pageSize: Int) = api.getArtists(page, pageSize)
    suspend fun getSearchArtists(page: Int, pageSize: Int, artistName: String) =
        api.getSearchArtists(page, pageSize, artistName)

    suspend fun getArtistAlbums(artistName: String, page: Int, pageSize: Int) =
        api.getAlbums(artistName, page, pageSize)

    suspend fun getAlbumTracks(artistName: String, album: String) =
        api.getAlbumTracks(artistName, album)
}