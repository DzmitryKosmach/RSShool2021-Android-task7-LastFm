package com.rsschool.task7_lastfm.model.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiDataArtists(
    @Json(name = "artists") val artists: ArtistsApi?
)

@JsonClass(generateAdapter = true)
data class ArtistsApi(
    @Json(name = "artist") val artist: List<ArtistApi>?
)


@JsonClass(generateAdapter = true)
data class ArtistApi(
    @Json(name = "name") val name: String?,
    @Json(name = "image") val imageUrls: List<ImageApi>?,
    @Json(name = "url") val artistUrl: String?
)

@JsonClass(generateAdapter = true)
data class ImageApi(
    @Json(name = "#text") val imageUrl: String?,
    @Json(name = "size") val imageSize: String?
)