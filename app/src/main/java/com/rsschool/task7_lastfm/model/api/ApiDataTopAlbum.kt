package com.rsschool.task7_lastfm.model.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiDataTopAlbum(
    @Json(name ="topalbums") val topAlbums: TopAlbumsApi?
)

@JsonClass(generateAdapter = true)
data class TopAlbumsApi(
    @Json(name ="album") val albums: List<TopAlbumApi>?
)

@JsonClass(generateAdapter = true)
data class TopAlbumApi(
    @Json(name ="name") val name: String?,
    @Json(name ="url") val urlAlbum: String?,
    @Json(name ="artist") val artist: ArtistTopAlbumApi?,
    @Json(name ="image") val images: List<ImageAlbumApi>?
)

@JsonClass(generateAdapter = true)
data class ArtistTopAlbumApi(
    @Json(name ="name") val name: String?
)

@JsonClass(generateAdapter = true)
data class ImageAlbumApi(
    @Json(name ="#text") val imageUrl: String?,
    @Json(name ="size") val sizeName: String?
)