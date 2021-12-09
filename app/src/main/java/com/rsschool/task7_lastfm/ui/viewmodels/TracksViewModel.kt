package com.rsschool.task7_lastfm.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.rsschool.task7_lastfm.repository.Repository
import com.rsschool.task7_lastfm.model.Track
import com.rsschool.task7_lastfm.repository.IRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow

class TracksViewModel @ViewModelInject constructor(
    val repository: IRepository
) : ViewModel() {

    private val _tracksStateFlow = MutableStateFlow(emptyList<Track>())
    val tracksStateFlow = _tracksStateFlow.asStateFlow()

    private suspend fun getTracks(artistName: String, albumName: String):List<Track> {
        return try {
            val response = repository.getAlbumTracks(artistName, albumName)
            val listTracks = checkNotNull(response.body())
            listTracks
        } catch (e: Exception){
            emptyList()
        }
    }

    suspend fun setListTracks(artistName: String, albumName: String) {
        val tracks = getTracks(artistName, albumName)
        _tracksStateFlow.value = tracks
    }

}