package com.example.appmusic.ui.play

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.appmusic.MainActivity
import com.example.appmusic.databinding.FragmentPlayBinding
import com.example.appmusic.model.Music
import com.example.appmusic.viewmodel.MusicViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior

class PlayFragment : Fragment() {

    private val musicViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        )[MusicViewModel::class.java]
    }
    private lateinit var _fragmentPlayBinding: FragmentPlayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // gone layout main
        MainActivity.activityMainBinding.costraintLayoutActivityMain.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        MainActivity.activityMainBinding.costraintLayoutActivityMain.visibility = View.VISIBLE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragmentPlayBinding = FragmentPlayBinding.inflate(inflater, container, false)
        return _fragmentPlayBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //bindService
        musicViewModel.bindPlayService(requireActivity())

        initControl()
    }

    /////////////////////////////////////////////////
    private fun initControl() {
        musicViewModel.musicListLiveData.observe(viewLifecycleOwner) { listMusic ->
            musicViewModel.selectedPosition.observe(viewLifecycleOwner) { selectedPosition ->
                if (PlayService.mediaPlayer != null) {
                    displayDetail(
                        PlayService.mediaPlayer!!,
                        listMusic[selectedPosition]
                    )
                }
            }
        }

        musicViewModel.musicListLiveData.observe(viewLifecycleOwner) {
            musicViewModel.clientBindPlayService.observe(viewLifecycleOwner) {
                it.getPlayService().let { playService ->
                    playService.stateMusic(PlayService.StateMediaPlayer.CREATE.toString())
                    enableButton(playService)
                }
            }
        }

        var bottomSheetBehavior =
            BottomSheetBehavior.from(_fragmentPlayBinding.linearLayoutBottomSheetPlay)
    }


    private fun enableButton(playService: PlayService) {
        if (PlayService.mediaPlayer != null) {

            if (PlayService.mediaPlayer!!.isPlaying)
                playService.stateMusic(PlayService.StateMediaPlayer.PLAY.toString())

            _fragmentPlayBinding.btPlay.setOnClickListener {
                playService.stateMusic(PlayService.StateMediaPlayer.PLAY.toString())
            }
            _fragmentPlayBinding.btPause.setOnClickListener {
                playService.stateMusic(PlayService.StateMediaPlayer.PAUSE.toString())
            }

            _fragmentPlayBinding.btNex.setOnClickListener {
                playService.stateMusic(PlayService.StateMediaPlayer.NEXT.toString())
            }
            _fragmentPlayBinding.btPre.setOnClickListener {
                playService.stateMusic(PlayService.StateMediaPlayer.PREV.toString())
            }
        }
    }

    private fun displayDetail(mediaPlayer: MediaPlayer, music: Music) {
        // check playing
        mediaPlayer.isPlaying.let {
            if (it) {
                _fragmentPlayBinding.btPlay.visibility = View.INVISIBLE
                _fragmentPlayBinding.btPause.visibility = View.VISIBLE
            } else {
                _fragmentPlayBinding.btPlay.visibility = View.VISIBLE
                _fragmentPlayBinding.btPause.visibility = View.INVISIBLE
            }
        }

        _fragmentPlayBinding.seekBar.apply {
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) mediaPlayer.seekTo(progress)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    return Unit
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    return Unit
                }
            })
            progress = 0
            max = mediaPlayer.duration
        }

        // detail
        _fragmentPlayBinding.tvTitle.text = music.title
        _fragmentPlayBinding.tvArtist.text = music.artist
    }

}