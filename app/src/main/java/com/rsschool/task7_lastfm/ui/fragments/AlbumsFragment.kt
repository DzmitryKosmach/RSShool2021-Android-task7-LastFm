package com.rsschool.task7_lastfm.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.paging.PagingData
import by.kirich1409.viewbindingdelegate.viewBinding
import com.rsschool.task7_lastfm.R
import com.rsschool.task7_lastfm.ui.adapters.AlbumPageAdapter
import com.rsschool.task7_lastfm.ui.adapters.OnAlbumPageItemClickListener
import com.rsschool.task7_lastfm.databinding.FragmentAlbumsBinding
import com.rsschool.task7_lastfm.ui.viewmodels.AlbumsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AlbumsFragment : Fragment(R.layout.fragment_albums), OnAlbumPageItemClickListener {

    private val binding by viewBinding(FragmentAlbumsBinding::bind)
    private val albumsViewModel: AlbumsViewModel by activityViewModels()

    private var callbacks: NavigationCallbacks? = null

    interface NavigationCallbacks {
        fun onTracksFragmentClick(artistName: String, albumName: String, albumImageUrl: String, albumUrl: String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as NavigationCallbacks?
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        val args: AlbumsFragmentArgs by navArgs()
        val artistName = args.artistName
        albumsViewModel.artist.name = artistName
        albumsViewModel.createFlowPageSource(albumsViewModel.artist)

        val itemAdapter = AlbumPageAdapter(this)
        itemAdapter.addLoadStateListener { loadState ->
            binding.recyclerViewAlbums.isVisible = loadState.refresh != LoadState.Loading
            binding.progressAlbums.isVisible = loadState.refresh == LoadState.Loading
            val errorState = when {
                loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                else -> null
            }
            errorState?.let {
                Toast.makeText(
                    context,
                    "${resources.getString(R.string.toast_bad_request)}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.recyclerViewAlbums.adapter = itemAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            itemAdapter.submitData(PagingData.empty())
            albumsViewModel.albumsPagingFlow.collect { pagingData ->
                itemAdapter.submitData(
                    viewLifecycleOwner.lifecycle,
                    pagingData
                )
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onItemClick(artistName: String, albumName: String, albumImageUrl: String, albumUrl: String) {
        callbacks?.onTracksFragmentClick(artistName, albumName, albumImageUrl, albumUrl)
    }
}