package com.rsschool.task7_lastfm.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.rsschool.task7_lastfm.R
import com.rsschool.task7_lastfm.ui.fragments.AlbumsFragment
import com.rsschool.task7_lastfm.ui.fragments.AlbumsFragmentDirections
import com.rsschool.task7_lastfm.ui.fragments.ArtistsFragment
import com.rsschool.task7_lastfm.ui.fragments.ArtistsFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ArtistsFragment.NavigationCallbacks,
    AlbumsFragment.NavigationCallbacks {

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        navController.navigateUp()
        return super.onSupportNavigateUp()
    }

    override fun onAlbumFragmentClick(artistName: String) {
        val action = ArtistsFragmentDirections.actionArtistsFragmentToAlbumsFragment(artistName)
        navController.navigate(action)
    }

    override fun onTracksFragmentClick(
        artistName: String,
        albumName: String,
        albumImageUrl: String,
        albumUrl: String
    ) {
        val action = AlbumsFragmentDirections.actionAlbumsFragmentToTracksFragment(
            artistName,
            albumName,
            albumImageUrl,
            albumUrl
        )
        navController.navigate(action)
    }
}