package com.rsschool.task7_lastfm.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rsschool.task7_lastfm.ui.pagesources.ArtistsPageSource
import com.rsschool.task7_lastfm.ui.ArtistsViewControlsState
import com.rsschool.task7_lastfm.repository.Repository
import com.rsschool.task7_lastfm.model.Artist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class ArtistsViewModel @ViewModelInject constructor(
    val repository: Repository
): ViewModel() {

    private lateinit var artistsPageSource: ArtistsPageSource

    private val _controlsState = MutableStateFlow(ArtistsViewControlsState(false, ""))
    val controlsState: Flow<ArtistsViewControlsState> = _controlsState

    lateinit var artistsFlow: Flow<PagingData<Artist>>

    init {
        createFlowPagingDataArtist()
    }

    fun createFlowPagingDataArtist() {
        artistsPageSource = ArtistsPageSource(repository, _controlsState.value)
        artistsFlow = Pager(
            PagingConfig(
                pageSize = 18,
                prefetchDistance = 10,
                initialLoadSize = 18
            )
        ) {
            artistsPageSource
        }.flow.cachedIn(viewModelScope)
    }

    fun setSearchText(searchText: String, isSearching: Boolean) {
        setControlsState { copy(isSearching = isSearching, searchText = searchText) }
    }

    private fun setControlsState(modifier: ArtistsViewControlsState.() -> ArtistsViewControlsState) {
        _controlsState.value = _controlsState.value.modifier()
    }

}