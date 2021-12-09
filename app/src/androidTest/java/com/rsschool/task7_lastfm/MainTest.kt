package com.rsschool.task7_lastfm

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import com.rsschool.task7_lastfm.model.Track
import com.rsschool.task7_lastfm.repository.FakeRepository
import com.rsschool.task7_lastfm.ui.viewmodels.TracksViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers.equalTo
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class MainTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Inject
    lateinit var tracksViewModel: TracksViewModel

    @Test
    fun getTracksByArtistAndAlbumInTracksViewModelTest(): Unit = runBlocking {
        tracksViewModel.setListTracks("abba", "album1")
        var tracks: List<Track> = emptyList()
        tracks = tracksViewModel.tracksStateFlow.first()
        assertThat(tracks, equalTo(FakeRepository.tracks))
    }

    @Test
    fun getEmptyListTracksByIncorrectArtistAndAlbumInTracksViewModelTest(): Unit = runBlocking {
        tracksViewModel.setListTracks("abba1", "album1")
        var tracks: List<Track> = emptyList()
        tracks = tracksViewModel.tracksStateFlow.first()
        assertThat(tracks, equalTo(emptyList()))
    }
}
