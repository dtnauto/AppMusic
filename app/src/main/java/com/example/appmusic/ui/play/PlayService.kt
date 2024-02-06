package com.example.appmusic.ui.play

import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Binder
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.appmusic.MainActivity
import com.example.appmusic.R
import com.example.appmusic.model.Music
import com.example.appmusic.model.thumbnail
import java.text.SimpleDateFormat
import java.util.Date


class PlayService : Service() {

    companion object {
        var mediaPlayer: MediaPlayer? = null
    }

    var listMusic: List<Music> = listOf()
    var selectedPosition: Int? = null
        set(value) {
//            Log.e("myprocode", "${value}")
            if (listMusic.isEmpty() || value == null) field = null
            else {
                if (value > (listMusic.size - 1)) {
                    field = 0
                } else if (value < 0) {
                    field = (listMusic.size - 1)
                } else field = value
            }
            Log.e("myprocode", "field $field")
        }


    enum class StateMediaPlayer {
        CREATE,
        PREPARE,
        START,
        PLAY,
        PAUSE,
        NEXT,
        PREV,
        STOP,
        RELEASE;
    }

    //////////////////////////////////////////// xử lý bài hát
    fun stateMusic(state: String) {
        when (state) {
            StateMediaPlayer.CREATE.toString() -> {
                if (mediaPlayer == null) {
                    Log.e("myprocode", "curren mediaPlayer = " + mediaPlayer.toString())
                    mediaPlayer = MediaPlayer()
                }
            }

            StateMediaPlayer.START.toString() -> {
                if (mediaPlayer != null && selectedPosition != null) {
                    mediaPlayer!!.start()
                    showNotification(listMusic[selectedPosition!!], 10)
//                serviceToActivityMySong(music,StateMediaPlayer.START)
                }
            }

            StateMediaPlayer.PLAY.toString() -> {
                if (mediaPlayer != null && selectedPosition != null) {
                    mediaPlayer!!.apply {
                        reset()
                        setDataSource(listMusic[selectedPosition!!].path)
                        prepare()
                        start()
                    }
                    showNotification(listMusic[selectedPosition!!], 10)
                    updateSelectedPosition()
                }
            }

            StateMediaPlayer.PAUSE.toString() -> {
                if (mediaPlayer != null) {
                    mediaPlayer!!.pause()
                    showNotification(listMusic[selectedPosition!!], 10)
                    updateSelectedPosition()
                }

            }

            StateMediaPlayer.NEXT.toString() -> {
                if (mediaPlayer != null && selectedPosition != null) {
                    selectedPosition = selectedPosition!! + 1
                    mediaPlayer!!.apply {
                        reset()
                        setDataSource(listMusic[selectedPosition!!].path)
                        prepare()
                        start()
                    }
                    showNotification(listMusic[selectedPosition!!], 10)
                    updateSelectedPosition()
                }
            }

            StateMediaPlayer.PREV.toString() -> {
                if (mediaPlayer != null && selectedPosition != null) {
                    selectedPosition = selectedPosition!! - 1
                    mediaPlayer!!.apply {
                        reset()
                        setDataSource(listMusic[selectedPosition!!].path)
                        prepare()
                        start()
                    }
                    showNotification(listMusic[selectedPosition!!], 10)
                    updateSelectedPosition()
                }
            }

            StateMediaPlayer.STOP.toString() -> {
                if (mediaPlayer != null) {
                    mediaPlayer!!.stop()
                    clearNotification(10)
                    updateSelectedPosition()
                }

            }

            StateMediaPlayer.RELEASE.toString() -> {
                if (mediaPlayer != null) {
                    clearNotification(10)
//                serviceToActivityMySong(music, StateMediaPlayer.RELEASE)
                    mediaPlayer!!.release()
                    mediaPlayer = null
                }
            }
        }
    }

    ///////////////////////////////////////////////////////////////service
    inner class ClientBindPlayService : Binder() {
        fun getPlayService(): PlayService {
            return this@PlayService // trả về một service ở lớp cha
        }
    }

    private var clientBindPlayService: ClientBindPlayService = ClientBindPlayService()

    override fun onBind(intent: Intent): IBinder? {
        Log.e("myprocode", "binded")
        return clientBindPlayService // trả về một đối tượng binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            "onClickInNotification" -> {
                intent.extras?.getString("DATA")?.let { stateMusic(it) }
            }
        }
        return START_NOT_STICKY
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.e("myprocode", "unbinded")
        stopSelf()
        return super.onUnbind(intent)
    }

    //////////////////////////////////////////////////////////activity
    private fun updateSelectedPosition() { //gửi trạng thái và bài hát khi khởi tạo service
        var intent = Intent().apply {
            action = "updateSelectedPosition"
            putExtra("DATA", selectedPosition)
        }
        LocalBroadcastManager.getInstance(this@PlayService).sendBroadcast(intent)
    }

    //////////////////////////////////////////////////////////notification
    private fun showNotification(music: Music, id: Int) {

        var mediaSessionCompat = MediaSessionCompat(this, "my music")

        // android11
//        mediaSessionCompat.setMetadata(
//            MediaMetadataCompat.Builder()
////                .putString(MediaMetadata.METADATA_KEY_TITLE, "Song Title")
////                .putString(MediaMetadata.METADATA_KEY_ARTIST, "Artist")
//                .build()
//        )

        val addActionButton = arrayOf(
            NotificationCompat.Action.Builder(
                R.drawable.skip_previous_24px, "Previous",
                onClickNotificationPendingIntent(
                    this,
                    StateMediaPlayer.PREV
                )
            ).build(),
            if (!mediaPlayer!!.isPlaying) {
                NotificationCompat.Action.Builder(
                    R.drawable.play_arrow_24px, "Pause",
                    onClickNotificationPendingIntent(
                        this,
                        StateMediaPlayer.PLAY
                    )
                ).build()
            } else {
                NotificationCompat.Action.Builder(
                    R.drawable.pause_24px, "Pause",
                    onClickNotificationPendingIntent(
                        this,
                        StateMediaPlayer.PAUSE
                    )
                ).build()
            },
            NotificationCompat.Action.Builder(
                R.drawable.skip_next_24px, "Next",
                onClickNotificationPendingIntent(
                    this,
                    StateMediaPlayer.NEXT
                )
            ).build(),
        )

        val notificationBuilder = NotificationCompat.Builder(this, "CHANNEL_APPMUSIC_NO1")
            .setSmallIcon(R.drawable.music_note_24px)
            .setLargeIcon(
                music.thumbnail()?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }
                    ?: BitmapFactory.decodeResource(resources, R.drawable.music_note_24px)
            )
            .setContentTitle(music.title)
            .setContentText(music.artist)
            .setSubText("AppMusicProNo1")

//            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setAutoCancel(false)
//            .setOngoing(true)

            // Apply the media style template.
            .addAction(addActionButton[0])
            .addAction(addActionButton[1])
            .addAction(addActionButton[2])

            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setShowActionsInCompactView(0, 1, 2)
                    .setMediaSession(mediaSessionCompat.sessionToken)
            )

            // Set the intent that fires when the user taps the notification.
            .setContentIntent(
                PendingIntent.getActivity(
                    this,
                    456,
                    Intent(this, MainActivity::class.java),
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
            .setSilent(true)
            .apply {
                startForeground(id, this.build()) // khởi tạo foregoundService
            }
    }

    private fun clearNotification(id: Int) {
        stopForeground(id)
    }

    private fun onClickNotificationPendingIntent(
        context: Context,
        state: StateMediaPlayer
    ): PendingIntent? {
        // Log trong này luôn được nha
        var intent = Intent(context, PlayService::class.java).apply {
            action = "onClickInNotification"
            putExtra("DATA", state.toString())
        }
        return PendingIntent.getService(
            context,
            state.ordinal, //state ở đây có nhiệm vụ phân biệt các pendingIntent khác nhau
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}