package com.rsschool.task7_lastfm.model.api

import androidx.annotation.IntRange
import com.rsschool.task7_lastfm.model.Album
import com.rsschool.task7_lastfm.model.Artist
import com.rsschool.task7_lastfm.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject

interface LastFmApi {
    @GET("/2.0/?method=chart.gettopartists&format=json")
    suspend fun getListOfArtistsResponse(
        @Query("page") @IntRange(from = 1) page: Int = 1,
        @Query("limit") @IntRange(from = 1) limit: Int = 100,
        @Query("api_key") apiKey: String?
    ): Response<ApiDataArtists>

    @GET("/2.0/?method=artist.search&format=json")
    suspend fun getSearchListOfArtistsResponse(
        @Query("page") @IntRange(from = 1) page: Int = 1,
        @Query("limit") @IntRange(from = 1) limit: Int = 100,
        @Query("api_key") apiKey: String?,
        @Query("artist") artist: String?,
    ): Response<ApiDataArtistsSearch>

    @GET("/2.0/?method=artist.gettopalbums&format=json")
    suspend fun getListOfTopAlbums(
        @Query("artist") artist: String?,
        @Query("api_key") apiKey: String?,
        @Query("page") @IntRange(from = 1) page: Int = 1,
        @Query("limit") @IntRange(from = 1) limit: Int = 100,
    ): ApiDataTopAlbum

    @GET("/2.0/?method=album.getinfo&format=json")
    suspend fun getListOfAlbumTracksResponse(
        @Query("artist") artist: String?,
        @Query("album") album: String?,
        @Query("api_key") apiKey: String?
    ): Response<ApiDataAlbumTracks>
}

class ArtistsApiImpl @Inject constructor(private val _artistsService: LastFmApi) {
    private val API_KEY = "61d6600170ff0cf825a3ee62d4bd7fa6"
    private val IMAGE_MEDIUM_INDEX = 2
    private val IMAGE_LARGE_INDEX = 3

    suspend fun getArtists(page: Int, pageSize: Int) = getListOfArtists(page, pageSize)
    suspend fun getSearchArtists(page: Int, pageSize: Int, artist: String) =
        getListOfArtists(page, pageSize, artist)

    private suspend fun getListOfArtists(
        page: Int,
        pageSize: Int,
        artist: String? = null,
        apiKey: String = API_KEY
    ): Response<List<Artist>> {
        return withContext(Dispatchers.IO) {

            val apiDataArtists = if (artist == null) {
                val response = _artistsService.getListOfArtistsResponse(page, pageSize, apiKey)
                if (response.isSuccessful) response.body()?.artists?.artist else emptyList()
            } else {
                val response =
                    _artistsService.getSearchListOfArtistsResponse(page, pageSize, apiKey, artist)
                if (response.isSuccessful) response.body()?.results?.artistMatches?.artist else emptyList()
            }

            val apiDataAlbum = apiDataArtists?.map { artist ->
                async { _artistsService.getListOfTopAlbums(artist.name ?: "", apiKey, 1, 2) }
            }?.map { it.await() }

            val artists = mutableListOf<Artist>()

            for (i in apiDataArtists!!.indices) {
                val artistAlbumImage = if (apiDataAlbum?.isNotEmpty() == true) {
                    val imageUrlString = if (apiDataAlbum.size > i) {
                        val currentApiDataTopAlbum =
                            requireNotNull(apiDataAlbum[i].topAlbums?.albums)
                        if (currentApiDataTopAlbum.isNotEmpty()) {
                            val firstImageUrl = currentApiDataTopAlbum[0].images?.get(
                                IMAGE_MEDIUM_INDEX
                            )?.imageUrl
                            if (requireNotNull(firstImageUrl).length > 1) firstImageUrl
                            else currentApiDataTopAlbum[1].images?.get(
                                IMAGE_MEDIUM_INDEX
                            )?.imageUrl
                        } else null
                    } else null
                    imageUrlString
                } else null

                artists.add(
                    Artist(
                        apiDataArtists[i].name,
                        artistAlbumImage,
                        apiDataArtists[i].artistUrl
                    )
                )
            }
            Response.success(artists)
        }
    }

    suspend fun getAlbums(artist: String, page: Int, pageSize: Int) =
        getListOfAlbumTracks(artist, page, pageSize)

    private suspend fun getListOfAlbumTracks(
        artist: String,
        page: Int,
        pageSize: Int,
        apiKey: String = API_KEY
    ): Response<List<Album>> {
        return withContext(Dispatchers.IO) {

            val apiDataTopAlbums =
                _artistsService.getListOfTopAlbums(artist, apiKey, page, pageSize).topAlbums?.albums

            val albums = mutableListOf<Album>()

            apiDataTopAlbums?.forEach {
                albums.add(
                    Album(
                        it.artist?.name,
                        it.name,
                        it.images?.get(IMAGE_MEDIUM_INDEX)?.imageUrl,
                        it.images?.get(IMAGE_LARGE_INDEX)?.imageUrl,
                        it.urlAlbum
                    )
                )
            }
            Response.success(albums)
        }
    }

    suspend fun getAlbumTracks(artist: String, album: String) = getListOfAlbumTracks(artist, album)

    private suspend fun getListOfAlbumTracks(
        artist: String,
        album: String,
        apiKey: String = API_KEY
    ): Response<List<Track>> {
        return withContext(Dispatchers.IO) {

            val response = _artistsService.getListOfAlbumTracksResponse(artist, album, apiKey)

            var apiDataArtistAlbum: ArtistAlbumApi? = null

            if (response.isSuccessful) {
                apiDataArtistAlbum = response.body()?.album
            }

            val tracks = mutableListOf<Track>()
            val artistName = apiDataArtistAlbum?.artist
            val albumName = apiDataArtistAlbum?.albumName
            val apiDataTracks = apiDataArtistAlbum?.tracks?.tracks

            apiDataTracks?.forEach {
                tracks.add(
                    Track(
                        artistName,
                        albumName,
                        it.name,
                        it.duration,
                        it.url
                    )
                )
            }
            Response.success(tracks)
        }
    }
}