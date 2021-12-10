package com.rsschool.task7_lastfm.repository

import com.rsschool.task7_lastfm.model.Album
import com.rsschool.task7_lastfm.model.Artist
import com.rsschool.task7_lastfm.model.Track
import retrofit2.Response

class FakeRepository : IRepository {
    override suspend fun getTopArtists(page: Int, pageSize: Int): Response<List<Artist>> {
        return Response.success(artists)
    }

    override suspend fun getSearchArtists(
        page: Int,
        pageSize: Int,
        artistName: String
    ): Response<List<Artist>> {
        val searchArtists = artists.filter { it.name == artistName }
        return Response.success(searchArtists)
    }

    override suspend fun getArtistAlbums(
        artistName: String,
        page: Int,
        pageSize: Int
    ): Response<List<Album>> {
        val artistAlbums = albums.filter { it.artist == artistName }
        return Response.success(artistAlbums)
    }

    override suspend fun getAlbumTracks(artistName: String, album: String): Response<List<Track>> {
        val albumTracks = tracks.filter { it.artist == artistName && it.album == album }
        return Response.success(albumTracks)
    }

    companion object {
        val artists = listOf(
            Artist("abba", "", "abba"),
            Artist("nirvana", "", "nirvana"),
            Artist("sher", "", "sher")
        )
        val albums = listOf(
            Album(artists[0].name, "album1", "", "", ""),
            Album(artists[0].name, "album2", "", "", ""),
            Album(artists[0].name, "album3", "", "", "")
        )
        val tracks = listOf(
            Track(artists[0].name, albums[0].name, "track1", 0, ""),
            Track(artists[0].name, albums[0].name, "track2", 0, ""),
            Track(artists[0].name, albums[0].name, "track3", 0, "")
        )
    }
}