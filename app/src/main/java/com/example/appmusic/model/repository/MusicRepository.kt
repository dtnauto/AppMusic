package com.example.appmusic.model.repository

import android.app.Application
import com.example.appmusic.model.Music
import com.example.appmusic.model.repository.content.AudioContentResolver
import com.example.appmusic.model.repository.sharepreferences.DataSharedPreferences

class MusicRepository(application: Application) {
    var musicList: List<Music> = AudioContentResolver(application).getAudioFile() // có thể lấy ra mảng ko có j

    var sharedPreferences = DataSharedPreferences("APP_MUSIC",application)
}