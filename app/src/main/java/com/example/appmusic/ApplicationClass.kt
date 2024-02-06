package com.example.appmusic

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

class ApplicationClass : Application() {
    override fun onCreate() {
        super.onCreate()

        chanelAPPMUSICNotification()
    }

    private fun chanelAPPMUSICNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //check vesion cho hiển thị thông báo
            NotificationChannel(
                "CHANNEL_APPMUSIC_NO1", // id để sự dụng cho code thông báo
                "App Music No1 VN", // tên hiển thị trong app khi lựa chọn loại thông báo
                NotificationManager.IMPORTANCE_HIGH //độ ưu tiên
            ).apply {
                description =
                    "Đây là thông báo của trình phát nhạc provip nhất quả đất" //mô tả trong app cho người dùng
                (applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                    .createNotificationChannel(
                        this
                    )
            }
        }
    }

}