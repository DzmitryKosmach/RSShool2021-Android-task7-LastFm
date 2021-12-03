package com.rsschool.task7_lastfm.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.rsschool.task7_lastfm.R
import com.rsschool.task7_lastfm.model.Track
import com.rsschool.task7_lastfm.databinding.FragmentTracksBinding
import com.rsschool.task7_lastfm.displayTime
import com.rsschool.task7_lastfm.openInBrowser
import com.rsschool.task7_lastfm.ui.viewmodels.TracksViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import coil.load

@AndroidEntryPoint
class TracksFragment : Fragment(R.layout.fragment_tracks) {

    private val binding by viewBinding(FragmentTracksBinding::bind)
    private val tracksViewModel: TracksViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        val args: TracksFragmentArgs by navArgs()
        val artistName = args.artistName
        val albumName = args.albumName

        val albumImageUrl = args.albumImageUrl
        val albumUrl = args.albumUrl

        binding.textViewTracksArtistName.text = artistName
        binding.textViewTracksAlbumName.text = albumName
        binding.imageViewTracksAlbum.apply {
            load(albumImageUrl){
                crossfade(true)
                fallback(R.drawable.placeholder)
                placeholder(R.drawable.placeholder)
            }
            setOnClickListener() {
                albumUrl.openInBrowser(context)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            tracksViewModel.setListTracks(artistName, albumName)
            tracksViewModel.tracksStateFlow.collect { tracks ->
                renderTracks(tracks)
            }
        }

    }

    private fun renderTracks(tracks: List<Track>) {

        tracks.forEachIndexed { index, track ->
            val textViewTrack = LayoutInflater.from(context)
                .inflate(R.layout.text_view_item_track, null) as TextView
            textViewTrack.apply {
                visibility = View.VISIBLE
                text = "${index.inc()}. ${track.name} - ${track.duration?.displayTime()}"
                setOnClickListener() {
                    track.url?.openInBrowser(context)
                }
            }
            binding.layoutAlbumTracks.addView(textViewTrack)
        }
    }
}