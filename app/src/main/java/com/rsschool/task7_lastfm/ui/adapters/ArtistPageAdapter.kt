package com.rsschool.task7_lastfm.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.rsschool.task7_lastfm.R
import com.rsschool.task7_lastfm.databinding.RecyclerViewItemArtistBinding
import com.rsschool.task7_lastfm.model.Artist

class ArtistPageAdapter (
    private var clickListener: OnArtistPageItemClickListener,
) : PagingDataAdapter<Artist, ArtistPageViewHolder>(ArtistDiffItemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistPageViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RecyclerViewItemArtistBinding.inflate(layoutInflater, parent, false)

        return ArtistPageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArtistPageViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }
}

class ArtistPageViewHolder(private val binding: RecyclerViewItemArtistBinding) : RecyclerView.ViewHolder(binding.root) {
    private val imageView = binding.imageViewItemArtist
    private val textView = binding.textViewItemArtist

    fun bind(artist: Artist?, action: OnArtistPageItemClickListener) {
        if(artist !=null) {
            imageView.setOnClickListener { action.onItemClick(artist.name ?: "") }
            imageView.load(artist.imageUrl) {
                crossfade(true)
                fallback(R.drawable.placeholder)
                placeholder(R.drawable.placeholder)
            }
            textView.text = artist.name
        }
    }
}

private object ArtistDiffItemCallback : DiffUtil.ItemCallback<Artist>() {

    override fun areItemsTheSame(oldItem: Artist, newItem: Artist): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Artist, newItem: Artist): Boolean {
        return oldItem.artistUrl == newItem.artistUrl
    }
}

interface OnArtistPageItemClickListener {
    fun onItemClick(artistName: String)
}