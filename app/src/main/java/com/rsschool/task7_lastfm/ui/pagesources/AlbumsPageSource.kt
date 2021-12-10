package com.rsschool.task7_lastfm.ui.pagesources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rsschool.task7_lastfm.model.Album
import com.rsschool.task7_lastfm.model.Artist
import com.rsschool.task7_lastfm.repository.IRepository

class AlbumsPageSource(
    private val artistsRepository: IRepository,
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

            val listAlbums = getAlbumsByArtistName(page, pageSize, artist?.name ?: "")

            val nextKey = if (listAlbums.size < pageSize) null else page + 1
            val prevKey = if (page == 1) null else page - 1
            LoadResult.Page(listAlbums, prevKey, nextKey)
        } catch (e: Exception){
            LoadResult.Error(Exception(e))
        }
    }

    suspend fun getAlbumsByArtistName(page: Int, pageSize: Int, artistName: String): List<Album>{
        val response = artistsRepository.getArtistAlbums(artistName, page, pageSize)
        return checkNotNull(response.body())
    }
}