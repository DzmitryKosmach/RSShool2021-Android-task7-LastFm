package com.rsschool.task7_lastfm.repository

import com.rsschool.task7_lastfm.model.api.ArtistsApiImpl
import javax.inject.Inject

class Repository @Inject constructor(
    private val api: ArtistsApiImpl
): IRepository {

    override suspend fun getTopArtists(page: Int, pageSize: Int) = api.getArtists(page, pageSize)
    override suspend fun getSearchArtists(page: Int, pageSize: Int, artistName: String) =
        api.getSearchArtists(page, pageSize, artistName)

    override suspend fun getArtistAlbums(artistName: String, page: Int, pageSize: Int) =
        api.getAlbums(artistName, page, pageSize)

    override suspend fun getAlbumTracks(artistName: String, album: String) =
        api.getAlbumTracks(artistName, album)
}