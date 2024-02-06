package com.example.appmusic.model.repository.content

import android.app.Application
import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.example.appmusic.model.Music
import java.io.File
import java.util.concurrent.TimeUnit

class AudioContentResolver(var application: Application) {
    fun getAudioFile(): MutableList<Music> {
        var tempList = mutableListOf<Music>()
        // content get audio
        val collection =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Audio.Media.getContentUri(
                    MediaStore.VOLUME_EXTERNAL
                )
            }
            else {
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            }
        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"
        val selectionArgs = arrayOf(
            TimeUnit.MILLISECONDS.convert(5, TimeUnit.SECONDS).toString()
        )
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATE_ADDED,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.ALBUM_ID,
        )
        val sortOrder = MediaStore.Audio.Media.DISPLAY_NAME + " ASC"
        // cursot get audio
        var cursor: Cursor? = application.contentResolver.query(
            collection,
            projection,
            selection,
            null,
            sortOrder,
            null
        )

        try {
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val idC = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
                    val titleC = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
                    val albumC = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM))
                    val artistC = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
                    val durationC = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))
                    val dataC = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
                    val albumIdC = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))
                    val nameColumn = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME))

                    val albumArt = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), albumIdC);
                    val contentUri: Uri = ContentUris.withAppendedId(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        idC
                    )

                    // Stores column values and the contentUri in a local object
                    // that represents the media file.
                    val music = Music(dataC,idC,titleC,albumC,artistC,durationC,albumArt)
                    val file = File(music.path)
                    if (file.exists())
                        tempList.add(music)
//                    videoList += Music(contentUri, name, duration, size)


//                    Log.e("mycodeisblocking", "$file--$albumIdC--$albumArt--$contentUri--$dataC" )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }

        return tempList
    }
}