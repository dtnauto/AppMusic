package com.example.appmusic.ui.music.tablayout

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appmusic.R
import com.example.appmusic.databinding.FragmentPlaylistsBinding
import com.example.appmusic.ui.music.adapter.PlaylistsAdapterRecyclerView
import com.example.appmusic.ui.play.PlayFragment
import com.example.appmusic.ui.play.PlayService
import com.example.appmusic.viewmodel.MusicViewModel
import com.example.appmusic.viewmodel.factory.MusicFactoryViewModel

class PlaylistsFragment : Fragment() {

//    private val musicViewModel by viewModels<MusicViewModel> { // dùng cách này để ủy quyền cho viewModels thực hiện khởi tạo viewModel
//        MusicFactoryViewModel(requireActivity().application)
//    }

    private val musicViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        )[MusicViewModel::class.java]
    }

    private lateinit var _binding: FragmentPlaylistsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initControl()
    }

    private fun initControl() {
        musicViewModel.musicListLiveData.observe(viewLifecycleOwner) { listMusic ->
            val playlistsAdapter = PlaylistsAdapterRecyclerView(
                requireActivity(),
                musics = listMusic,
                onClick = {
                    musicViewModel.selectedPosition.value = it
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_play, PlayFragment()).addToBackStack(null)
                        .commit()
                },
                onMore = {
//                noteViewModel.deleteNote(it)
                }
            )

            _binding.rvPlaylists.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(
                    context, // màn hình hiển thị
                    LinearLayoutManager.VERTICAL, // chiều hiển thị
                    false // đảo ngược danh sách
                )
                adapter = playlistsAdapter
            }

        }
    }

}