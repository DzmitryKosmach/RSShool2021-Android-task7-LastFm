package com.rsschool.task7_lastfm.repository

import com.rsschool.task7_lastfm.model.Album
import com.rsschool.task7_lastfm.model.Artist
import com.rsschool.task7_lastfm.model.Track
import retrofit2.Response

interface IRepository {
    suspend fun getTopArtists(page: Int, pageSize: Int): Response<List<Artist>>

    suspend fun getSearchArtists(page: Int, pageSize: Int, artistName: String): Response<List<Artist>>

    suspend fun getArtistAlbums(artistName: String, page: Int, pageSize: Int): Response<List<Album>>

    suspend fun getAlbumTracks(artistName: String, album: String): Response<List<Track>>
}