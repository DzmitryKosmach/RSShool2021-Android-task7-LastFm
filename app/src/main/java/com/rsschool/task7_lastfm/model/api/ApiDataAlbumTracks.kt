package com.rsschool.task7_lastfm.model.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiDataAlbumTracks (
    @Json(name ="album") val album: ArtistAlbumApi?
)

@JsonClass(generateAdapter = true)
data class ArtistAlbumApi(
    @Json(name ="artist") val artist: String?,
    @Json(name ="tracks") val tracks: AlbumTracksApi?,
    @Json(name ="name") val albumName: String?
)

@JsonClass(generateAdapter = true)
data class AlbumTracksApi(
    @Json(name ="track") val tracks: List<AlbumTrackApi>?,
)

@JsonClass(generateAdapter = true)
data class AlbumTrackApi(
    @Json(name ="duration") val duration: Int?,
    @Json(name ="url") val url: String?,
    @Json(name ="name") val name: String?,
    @Json(name ="artist") val artist: ArtistAlbumTrackApi?,
)

@JsonClass(generateAdapter = true)
data class ArtistAlbumTrackApi(
    @Json(name ="name") val ArtistName: String?,
    @Json(name ="url") val artistUrl: String?
)