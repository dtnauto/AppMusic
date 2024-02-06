package com.example.appmusic

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.appmusic.databinding.ActivityMainBinding
import com.example.appmusic.model.Music
import com.example.appmusic.model.thumbnail
import com.example.appmusic.ui.home.HomeFragment
import com.example.appmusic.ui.library.LibraryFragment
import com.example.appmusic.ui.music.MusicFragment
import com.example.appmusic.ui.play.PlayFragment
import com.example.appmusic.ui.play.PlayService
import com.example.appmusic.ui.search.SearchFragment
import com.example.appmusic.ui.user.UserFragment
import com.example.appmusic.viewmodel.MusicViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior

class MainActivity : AppCompatActivity() {

    val musicViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(this.application)
        )[MusicViewModel::class.java]
    }

    companion object {
        lateinit var activityMainBinding: ActivityMainBinding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        listenBroadcast()
        initControl()
        initTransactionPlayFragment()
        initBottomNavMain()
    }

    ///////////////////////////////////////////////////////////
    private fun initTransactionPlayFragment() {
        if (musicViewModel.getStateMediaPlayer()) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout_play, PlayFragment()).addToBackStack(null)
                .commit()
        }
    }

    /////////////////////////////////////////////////////////
    private fun listenBroadcast() {
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(
                object : BroadcastReceiver() {
                    override fun onReceive(context: Context?, intent: Intent?) {
                        when (intent?.action) {
                            "updateSelectedPosition" -> {
                                musicViewModel.selectedPosition.value =
                                    intent.extras?.getInt("DATA")
                            }
                        }
                    }
                },
                IntentFilter("updateSelectedPosition")
            )

    }

    /////////////////////////////////////////////////
    private fun initControl() {
        musicViewModel.musicListLiveData.observe(this@MainActivity) { listMusic ->
            musicViewModel.selectedPosition.observe(this@MainActivity) { selectedPosition ->
                if (PlayService.mediaPlayer != null) {
                    displayDetail(
                        PlayService.mediaPlayer!!,
                        listMusic[selectedPosition]
                    )
                }
            }
        }

        musicViewModel.musicListLiveData.observe(this) {
            musicViewModel.clientBindPlayService.observe(this) {
                it.getPlayService().let { playService ->
                    playService.stateMusic(PlayService.StateMediaPlayer.CREATE.toString())
                    enableButton(playService)
                    enableBottomSheetPlayMain()
                }
            }
        }
    }

    private fun enableBottomSheetPlayMain() {
        var bottomSheetPlayMainBehavior =
            BottomSheetBehavior.from(activityMainBinding.bottomSheetPlayMain)
        if (PlayService.mediaPlayer != null) {
            bottomSheetPlayMainBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            musicViewModel.putStateMediaPlayer(true)
        } else {
            bottomSheetPlayMainBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetPlayMainBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
//                        musicViewModel.clientBindPlayService.value?.getPlayService()
//                            ?.stateMusic(PlayService.StateMediaPlayer.RELEASE.toString())
                        musicViewModel.putStateMediaPlayer(false)
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })
    }

    private fun enableButton(playService: PlayService) {
        if (PlayService.mediaPlayer != null) {

            if (PlayService.mediaPlayer!!.isPlaying)
                playService.stateMusic(PlayService.StateMediaPlayer.PLAY.toString())

            activityMainBinding.btPlay.setOnClickListener {
                playService.stateMusic(PlayService.StateMediaPlayer.PLAY.toString())
            }
            activityMainBinding.btPause.setOnClickListener {
                playService.stateMusic(PlayService.StateMediaPlayer.PAUSE.toString())
            }

            activityMainBinding.btNex.setOnClickListener {
                playService.stateMusic(PlayService.StateMediaPlayer.NEXT.toString())
            }
            activityMainBinding.btPre.setOnClickListener {
                playService.stateMusic(PlayService.StateMediaPlayer.PREV.toString())
            }
        }
    }

    private fun displayDetail(mediaPlayer: MediaPlayer, music: Music) {
        // check playing
        mediaPlayer.isPlaying.let {
            if (it) {
                activityMainBinding.btPlay.visibility = View.INVISIBLE
                activityMainBinding.btPause.visibility = View.VISIBLE
            } else {
                activityMainBinding.btPlay.visibility = View.VISIBLE
                activityMainBinding.btPause.visibility = View.INVISIBLE
            }
        }
        // detail
        activityMainBinding.imTitle.setImageBitmap(
            music.thumbnail()?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }
                ?: BitmapFactory.decodeResource(resources, R.drawable.music_note_24px))
        activityMainBinding.tvTitle.text = music.title
        activityMainBinding.tvArtist.text = music.artist
    }

    /////////////////////////////////////////////////
    private fun initBottomNavMain() {
        activityMainBinding.bottomNavMain.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.action_home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_ui, HomeFragment()).commit()
                    true
                }

                R.id.action_search -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_ui, SearchFragment()).commit()
                    true
                }

                R.id.action_music -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_ui, MusicFragment()).commit()
                    true
                }

                R.id.action_library -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_ui, LibraryFragment()).commit()
                    true
                }

                R.id.action_user -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout_ui, UserFragment()).commit()
                    true
                }

                else -> false
            }
        }
    }

    //    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when(requestCode) {
//            1-> {
//                if (grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
//                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show()
//                }else{
//                    ActivityCompat.requestPermissions( // chưa được cấp thì xin lại quyền này
//                        this,
//                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE) , //xin quyền nào
//                        1 //yêu cầu quyền mới được thực hiện còn không thì hỏi lại lần nữa trong onRequestPermissionsResult
//                    )
//                }
//            }
//
//        }
//    }

}