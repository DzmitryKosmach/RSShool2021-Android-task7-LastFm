package com.rsschool.task7_lastfm.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rsschool.task7_lastfm.ui.pagesources.AlbumsPageSource
import com.rsschool.task7_lastfm.repository.Repository
import com.rsschool.task7_lastfm.model.Album
import com.rsschool.task7_lastfm.model.Artist
import kotlinx.coroutines.flow.Flow

class AlbumsViewModel @ViewModelInject constructor(
    val repository: Repository
) : ViewModel() {
    val artist = Artist(null, null, null)

    private var albumsPageSource = AlbumsPageSource(repository, artist)

    lateinit var albumsFlow: Flow<PagingData<Album>>

    fun createFlowPageSource(artist: Artist) {
        albumsPageSource = AlbumsPageSource(repository, artist)
        albumsFlow = Pager(
            PagingConfig(
                pageSize = 18,
                prefetchDistance = 10,
                initialLoadSize = 18
            )
        ) {
            albumsPageSource
        }.flow.cachedIn(viewModelScope)
    }

}