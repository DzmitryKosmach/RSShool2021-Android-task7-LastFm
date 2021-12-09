package com.rsschool.task7_lastfm.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import by.kirich1409.viewbindingdelegate.viewBinding
import com.rsschool.task7_lastfm.R
import com.rsschool.task7_lastfm.databinding.FragmentArtistsBinding
import com.rsschool.task7_lastfm.ui.ArtistsViewControlsState
import com.rsschool.task7_lastfm.ui.adapters.ArtistPageAdapter
import com.rsschool.task7_lastfm.ui.adapters.OnArtistPageItemClickListener
import com.rsschool.task7_lastfm.ui.viewmodels.ArtistsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ArtistsFragment : Fragment(R.layout.fragment_artists), OnArtistPageItemClickListener {

    private val binding by viewBinding(FragmentArtistsBinding::bind)
    private val artistsViewModel: ArtistsViewModel by activityViewModels()
    private lateinit var itemAdapter: ArtistPageAdapter
    private lateinit var artistsJob: Job
    private var menuItemSwitchTheme: MenuItem? = null

    @Inject
    lateinit var currentControlsState: ArtistsViewControlsState

    interface NavigationCallbacks {
        fun onAlbumFragmentClick(artistName: String)
    }

    private var callbacks: NavigationCallbacks? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as NavigationCallbacks?
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        itemAdapter = ArtistPageAdapter( this)
        itemAdapter.addLoadStateListener { loadState ->
            binding.recyclerViewArtists.isVisible = loadState.refresh != LoadState.Loading
            binding.progressArtists.isVisible = loadState.refresh == LoadState.Loading
            val errorState = when {
                loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                else -> null
            }
            errorState?.let {
                Toast.makeText(
                    context,
                    resources.getString(R.string.toast_bad_request),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.recyclerViewArtists.adapter = itemAdapter

        artistsJob = resetJob(false, "")

        artistsViewModel.controlsStateFlow.onEach(::renderControlsState).launchIn(lifecycleScope)
    }

    private fun resetJob(isSearching: Boolean, searchText: String): Job {
        if (this::artistsJob.isInitialized)
            artistsJob.cancel()
        artistsViewModel.setSearchText(searchText, isSearching)
        if (currentControlsState.isSearching) artistsViewModel.createFlowPagingDataArtist()
        return viewLifecycleOwner.lifecycleScope.launch {
            itemAdapter.submitData(PagingData.empty())
            artistsViewModel.artistsPagingFlow.collect { pagingData ->
                itemAdapter.submitData(
                    viewLifecycleOwner.lifecycle,
                    pagingData
                )
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)

        val menuItemSearch = menu.findItem(R.id.action_search)
        val searchView = menuItemSearch?.actionView as SearchView
        searchView.queryHint = getString(R.string.query_hint)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                artistsJob = resetJob(true, query ?: "")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (searchView.query.isEmpty()) {
                    if (currentControlsState.isSearching) {
                        artistsJob = resetJob(false, "")
                    }
                }
                if (newText?.length!! < 1) artistsViewModel.setSearchText("", false)
                return false
            }

        })

        menuItemSwitchTheme = menu.findItem(R.id.app_bar_switch_dark_theme)

        if (AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_YES) {
            menuItemSwitchTheme?.icon = AppCompatResources.getDrawable(
                requireNotNull(context),
                R.drawable.ic_baseline_moon_2_24
            )
        } else {
            menuItemSwitchTheme?.icon =
                AppCompatResources.getDrawable(
                    requireNotNull(context),
                    R.drawable.ic_baseline_wb_sunny_24
                )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.app_bar_switch_dark_theme) {
            if (AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onItemClick(artistName: String) {
        callbacks?.onAlbumFragmentClick(artistName)
    }

    private fun renderControlsState(state: ArtistsViewControlsState) {
        artistsJob = resetJob(state.isSearching, state.searchText)
        currentControlsState = state
    }
}