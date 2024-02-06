package com.example.appmusic.ui.music.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.appmusic.databinding.MusicBinding
import com.example.appmusic.model.Music
import com.example.appmusic.model.thumbnail

class PlaylistsAdapterRecyclerView(
    var context: Context,
    var musics: List<Music>,
    var onClick: (Int) -> Unit,
    var onMore: (Int) -> Unit
) : RecyclerView.Adapter<PlaylistsAdapterRecyclerView.MusicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        return MusicViewHolder(
            MusicBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int {
        return musics.size
    }

    inner class MusicViewHolder(private var _binding: MusicBinding) :
        RecyclerView.ViewHolder(_binding.root) {
        fun onBind(position: Int) {
            _binding.tvTitle.text = musics[position].title
            _binding.tvArtist.text = musics[position].artist
            _binding.layoutMusic.setOnClickListener {
                onClick(position)
            }
            _binding.ibMore.setOnClickListener {
                onMore(position)
            }
            _binding.imTitle.setImageBitmap(
                musics[position].thumbnail()?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }
                    ?: null)
//            Glide.with(context)
//                .load(musics[position].albumId)
//                .placeholder(R.drawable.music_note_24px)
//                .error(R.drawable.music_note_24px)
//                .centerCrop().into(_binding.imTitle);
        }
    }
}