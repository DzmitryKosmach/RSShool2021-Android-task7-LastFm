package com.rsschool.task7_lastfm.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.rsschool.task7_lastfm.R
import com.rsschool.task7_lastfm.model.Album
import com.rsschool.task7_lastfm.databinding.RecyclerViewItemAlbumBinding

class AlbumPageAdapter(
    private var clickListener: OnAlbumPageItemClickListener,
) : PagingDataAdapter<Album, AlbumPageViewHolder>(AlbumDiffItemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumPageViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RecyclerViewItemAlbumBinding.inflate(layoutInflater, parent, false)

        return AlbumPageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumPageViewHolder, position: Int) {
        holder.bind(
            getItem(position),
            clickListener
        )
    }
}

class AlbumPageViewHolder(private val binding: RecyclerViewItemAlbumBinding) :
    RecyclerView.ViewHolder(binding.root) {
    private val imageView = binding.imageViewItemAlbum
    private val textView = binding.textViewItemAlbum

    fun bind(
        album: Album?,
        action: OnAlbumPageItemClickListener
    ) {
        if (album != null) {
            imageView.setOnClickListener {
                action.onItemClick(
                    album.artist ?: "",
                    album.name ?: "",
                    album.imageLargeUrl ?: "",
                    album.albumUrl ?: ""
                )
            }
            imageView.load(album.imageUrl) {
                crossfade(true)
                fallback(R.drawable.placeholder)
                placeholder(R.drawable.placeholder)
            }
            textView.text = album.name
        }
    }
}

private object AlbumDiffItemCallback : DiffUtil.ItemCallback<Album>() {

    override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean {
        return oldItem.albumUrl == newItem.albumUrl
    }
}

interface OnAlbumPageItemClickListener {
    fun onItemClick(artistName: String, albumName: String, albumImageUrl: String, albumUrl: String)
}