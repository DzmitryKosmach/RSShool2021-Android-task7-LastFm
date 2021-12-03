package com.rsschool.task7_lastfm.model.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiDataArtistsSearch(
    @Json(name = "results") val results: SearchResultApi?
)

@JsonClass(generateAdapter = true)
data class SearchResultApi(
    @Json(name = "artistmatches") val artistMatches: ArtistsMatchApi?
)

@JsonClass(generateAdapter = true)
data class ArtistsMatchApi(
    @Json(name = "artist") val artist: List<ArtistApi>?
)
