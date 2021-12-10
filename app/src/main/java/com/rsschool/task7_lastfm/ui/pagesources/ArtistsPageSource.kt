package com.rsschool.task7_lastfm.ui.pagesources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rsschool.task7_lastfm.model.Artist
import com.rsschool.task7_lastfm.repository.IRepository
import com.rsschool.task7_lastfm.ui.ArtistsViewControlsState
import retrofit2.Response

class ArtistsPageSource(
    private val artistsRepository: IRepository,
    private val artistsViewControlsState: ArtistsViewControlsState
) : PagingSource<Int, Artist>() {

    override fun getRefreshKey(state: PagingState<Int, Artist>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Artist> {

        return try {
            val page: Int = params.key ?: 1
            val pageSize = params.loadSize

            val listArtists = getListArtists(page, pageSize, artistsViewControlsState)

            val nextKey = if (listArtists.isEmpty()) null else page + 1
            val prevKey = if (page == 1) null else page - 1
            LoadResult.Page(listArtists, prevKey, nextKey)
        } catch (e: Exception) {
            LoadResult.Error(Exception(e))
        }
    }

    suspend fun getListArtists(
        page: Int,
        pageSize: Int,
        artistsViewControlsState: ArtistsViewControlsState
    ): List<Artist> {
        val response = if (artistsViewControlsState.isSearching) artistsRepository.getSearchArtists(
            page,
            pageSize, artistsViewControlsState.searchText
        ) else artistsRepository.getTopArtists(page, pageSize)
        return checkNotNull(response.body())
    }
}