package com.example.musicplayer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.codezone.musicplayersimple.Song
import com.codezone.musicplayersimple.databinding.ItemSongBinding

class MusicAdapter(
    private val songs: List<Song>, private val onClick: (Int) -> Unit
) : RecyclerView.Adapter<MusicAdapter.SongViewHolder>() {

    inner class SongViewHolder(val binding: ItemSongBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val binding = ItemSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SongViewHolder(binding)
    }

    override fun getItemCount() = songs.size

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songs[position]
        holder.binding.txtTitle.text = song.title
        holder.binding.txtArtist.text = song.artist
        holder.binding.root.setOnClickListener { onClick(position) }
    }
}
