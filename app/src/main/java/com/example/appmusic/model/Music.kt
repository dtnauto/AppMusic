package com.example.appmusic.model

import android.media.MediaMetadataRetriever
import android.net.Uri
import java.time.Duration

data class Music(
//    var uri: Uri,
    var path: String,
    var id: Long,
    var title: String,
    var album: String,
    var artist: String,
    var duration: Long,
    var albumId: Uri,
){

}

fun Music.thumbnail(): ByteArray? {
    val retriever = MediaMetadataRetriever()
    retriever.setDataSource(this.path)
    return retriever.embeddedPicture
}
