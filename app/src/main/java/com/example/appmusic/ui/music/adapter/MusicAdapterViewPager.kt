package com.example.appmusic.ui.music.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.appmusic.ui.music.tablayout.AlbumsFragment
import com.example.appmusic.ui.music.tablayout.ArtistsFragment
import com.example.appmusic.ui.music.tablayout.PlaylistsFragment

class MusicAdapterViewPager(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
): FragmentStateAdapter(fragmentManager,lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> {
                PlaylistsFragment()
            }
            1 -> {
                AlbumsFragment()
            }
            3 -> {
                ArtistsFragment()
            }

            else -> {
                PlaylistsFragment()
            }
        }
    }
}