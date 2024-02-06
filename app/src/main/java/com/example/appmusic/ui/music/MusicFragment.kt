package com.example.appmusic.ui.music

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.appmusic.databinding.FragmentMusicBinding
import com.example.appmusic.model.repository.MusicRepository
import com.example.appmusic.requestRuntimePermission
import com.example.appmusic.ui.music.adapter.MusicAdapterViewPager
import com.google.android.material.tabs.TabLayoutMediator

class MusicFragment : Fragment() {

    private lateinit var _binding: FragmentMusicBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestRuntimePermission(requireActivity(),1)
        requestRuntimePermission(requireActivity(),2)
        requestRuntimePermission(requireActivity(),3)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMusicBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding.vpMusic.adapter = MusicAdapterViewPager(parentFragmentManager,lifecycle)
        TabLayoutMediator(
            _binding.tlMusic,
            _binding.vpMusic
        ){
            tab, pos ->
            when(pos){
                0 -> {tab.text = "Playlists"}
                1 -> {tab.text = "Albums"}
                2 -> {tab.text = "Artists"}
            }
        }.attach()// hiển thi tab
    }
}