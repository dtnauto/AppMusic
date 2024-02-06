package com.example.appmusic

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService

fun requestRuntimePermission(context: Context, id: Int): Boolean {
    when (id) {
//        0 -> {
//            if (ActivityCompat.checkSelfPermission( // yêu cầu quyền thông báo
//                    context,
//                    Manifest.permission.POST_NOTIFICATIONS
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                ActivityCompat.requestPermissions(
//                    context as Activity,
//                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
//                    0 //yêu cầu quyền mới được thự hiện tiếp
//                )
//            }
//        }

        1 -> {
            if (ActivityCompat.checkSelfPermission( //kiểm tra xin quyền được hay chưa
                    context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED // đã đc cấp chưa
            ) {
                ActivityCompat.requestPermissions( // chưa được cấp thì xin quyền
                    context as Activity,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), //xin quyền nào
                    id //yêu cầu quyền mới được thực hiện còn không thì hỏi lại lần nữa trong onRequestPermissionsResult
                )
            }
//            else {
//                Toast.makeText(context as Activity, "Permission already granted", Toast.LENGTH_SHORT).show()
//            }
        }

        2 -> {
            if (ActivityCompat.checkSelfPermission( //kiểm tra xin quyền được hay chưa
                    context,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED // đã đc cấp chưa
            ) {
                ActivityCompat.requestPermissions( // chưa được cấp thì xin quyền
                    context as Activity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), //xin quyền nào
                    id //yêu cầu quyền mới được thực hiện còn không thì hỏi lại lần nữa trong onRequestPermissionsResult
                )
            }
        }

        3 -> {
            if (ActivityCompat.checkSelfPermission( //kiểm tra xin quyền được hay chưa
                    context,
                    Manifest.permission.MANAGE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED // đã đc cấp chưa
            ) {
                ActivityCompat.requestPermissions( // chưa được cấp thì xin quyền
                    context as Activity,
                    arrayOf(Manifest.permission.MANAGE_EXTERNAL_STORAGE), //xin quyền nào
                    id //yêu cầu quyền mới được thực hiện còn không thì hỏi lại lần nữa trong onRequestPermissionsResult
                )
            }
        }
    }
    return false
}