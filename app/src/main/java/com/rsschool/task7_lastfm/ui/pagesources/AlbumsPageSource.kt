package com.rsschool.task7_lastfm.ui.pagesources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rsschool.task7_lastfm.model.Album
import com.rsschool.task7_lastfm.model.Artist
import com.rsschool.task7_lastfm.repository.Repository
import java.lang.Exception
import javax.inject.Inject

class AlbumsPageSource (
    private val artistsRepository: Repository,
    private val artist: Artist?
) : PagingSource<Int, Album>() {

    override fun getRefreshKey(state: PagingState<Int, Album>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?:page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Album> {
        return try {
            val page: Int = params.key ?: 1
            val pageSize = params.loadSize
            val response = artistsRepository.getArtistAlbums(artist?.name ?: "", page, pageSize)
            val listArtists = checkNotNull(response.body())
            val nextKey = if (listArtists.size < pageSize) null else page + 1
            val prevKey = if (page == 1) null else page - 1
            LoadResult.Page(listArtists, prevKey, nextKey)
        } catch (e: Exception){
            LoadResult.Error(Exception(e))
        }
    }
}