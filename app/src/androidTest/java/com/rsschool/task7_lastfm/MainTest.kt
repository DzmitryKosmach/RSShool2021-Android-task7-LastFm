package com.rsschool.task7_lastfm

import androidx.test.espresso.matcher.ViewMatchers.assertThat
import com.rsschool.task7_lastfm.model.Album
import com.rsschool.task7_lastfm.model.Artist
import com.rsschool.task7_lastfm.model.Track
import com.rsschool.task7_lastfm.repository.FakeRepository
import com.rsschool.task7_lastfm.ui.viewmodels.AlbumsViewModel
import com.rsschool.task7_lastfm.ui.viewmodels.ArtistsViewModel
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

    @Inject
    lateinit var artistsViewModel: ArtistsViewModel

    @Inject
    lateinit var albumsViewModel: AlbumsViewModel

    // Test TracksViewModel
    @Test
    fun getTracksByArtistAndAlbumInTracksViewModelTest(): Unit = runBlocking {
        tracksViewModel.setListTracks("abba", "album1")
        val tracks: List<Track> = tracksViewModel.tracksStateFlow.first()
        assertThat(tracks, equalTo(FakeRepository.tracks))
    }

    @Test
    fun getEmptyListTracksByIncorrectArtistAndAlbumInTracksViewModelTest(): Unit = runBlocking {
        tracksViewModel.setListTracks("abba1", "album1")
        val tracks: List<Track> = tracksViewModel.tracksStateFlow.first()
        assertThat(tracks, equalTo(emptyList()))
    }

    // Test ArtistsViewModel
    @Test
    fun getArtistsBySetSearchTextInArtistsViewModelTest(): Unit = runBlocking {
        val searchText = "abba"
        artistsViewModel.setSearchText(searchText, true)
        artistsViewModel.createFlowPagingDataArtist()
        val controlsState = artistsViewModel.controlsStateFlow.first()
        val listSearchArtists =
            artistsViewModel.artistsPageSource.getListArtists(1, 1, controlsState)
        assertThat(
            listSearchArtists,
            equalTo(FakeRepository.artists.filter { it.name == searchText })
        )
    }

    @Test
    fun getArtistsInArtistsViewModelTest(): Unit = runBlocking {
        artistsViewModel.setSearchText("", false)
        artistsViewModel.createFlowPagingDataArtist()
        val controlsState = artistsViewModel.controlsStateFlow.first()
        val listSearchArtists =
            artistsViewModel.artistsPageSource.getListArtists(1, 1, controlsState)
        assertThat(listSearchArtists, equalTo(FakeRepository.artists))
    }

    // Test AlbumsViewModel
    @Test
    fun getAlbumsByArtistNameInArtistsViewModelTest(): Unit = runBlocking {
        val artistName = "abba"
        val listAlbums = albumsViewModel.albumsPageSource.getAlbumsByArtistName(1, 1, artistName)
        assertThat(listAlbums, equalTo(FakeRepository.albums.filter { it.artist == artistName }))
    }

    @Test
    fun getEmptyListByIncorectArtistNameInArtistsViewModelTest(): Unit = runBlocking {
        val artistName = "abba1"
        val listAlbums = albumsViewModel.albumsPageSource.getAlbumsByArtistName(1, 1, artistName)
        assertThat(listAlbums, equalTo(emptyList()))
    }
}
