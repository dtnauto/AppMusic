package com.example.appmusic.ui.play

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.appmusic.MainActivity

//class PlayBroadcastReceiver : BroadcastReceiver() {
//    override fun onReceive(context: Context?, intent: Intent?) {
//        when (intent?.action) {
//            "onClickInNotification" -> {
//                var selectedPosition = intent.extras?.getInt("DATA")
//
//                Log.e("myprocode", "ahihi $selectedPosition")
//                var intent = Intent(context, MainActivity::class.java).apply {
//                    action = "updateSelectedPosition"
//                    putExtra("DATA", selectedPosition)
//                }
//                // gửi lại state của bài hát
//
////                    if (context != null) {
////                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
////                    } //gửi lại cho service. Khi service chạy lại thì sẽ đi vào luôn hàm onStart mà ko đi vào onCreate // startService để truyền intent
//            }
//        }
//    }
//}