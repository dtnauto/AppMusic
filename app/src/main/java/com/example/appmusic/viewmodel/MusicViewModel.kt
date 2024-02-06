package com.example.appmusic.viewmodel

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.appmusic.model.Music
import com.example.appmusic.model.repository.MusicRepository
import com.example.appmusic.ui.play.PlayService

class MusicViewModel(application: Application) : AndroidViewModel(application) {
    private val musicRepository = MusicRepository(application)

    //////////////////////////////////////////////////////////////////
    private val keyStateMediaPlayer = "keyStateMediaPlayer"
    fun putStateMediaPlayer(value: Boolean){
        musicRepository.sharedPreferences.putStateMediaPlayer(keyStateMediaPlayer,value)
    }
    fun getStateMediaPlayer(): Boolean{
        return musicRepository.sharedPreferences.getStateMediaPlayer(keyStateMediaPlayer)
    }

    /////////////////////////////////////////////////////////////////
    private var _musicListLiveData: MutableLiveData<List<Music>> =
        MutableLiveData(musicRepository.musicList)
    val musicListLiveData: LiveData<List<Music>> = _musicListLiveData

    var selectedPosition:MutableLiveData<Int> =  MutableLiveData()
    ///////////////////////////////////////////////////////////////////

    private var _clientBindPlayService: MutableLiveData<PlayService.ClientBindPlayService> =
        MutableLiveData()
    val clientBindPlayService: LiveData<PlayService.ClientBindPlayService> = _clientBindPlayService
    private var serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.e("myprocode", "service connected")
            _clientBindPlayService.value = (service as PlayService.ClientBindPlayService).apply {
                getPlayService().apply {
                    listMusic = musicRepository.musicList
                    selectedPosition = this@MusicViewModel.selectedPosition.value
                }
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.e("myprocode", "service unconnected")
            _clientBindPlayService.postValue(null)
        }
    }

    fun bindPlayService(context: Context) {
        // bindService
        val intent = Intent(context, PlayService::class.java).apply {
        }
        context.bindService(
            intent,
            serviceConnection,
            Context.BIND_AUTO_CREATE
        )
    }
    fun unbindPlayService(context: Context) {
        // unbindService
        context.unbindService(serviceConnection)
    }
}