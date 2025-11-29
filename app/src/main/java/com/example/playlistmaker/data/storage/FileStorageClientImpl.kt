package com.example.playlistmaker.data.storage

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import com.example.playlistmaker.data.FileStorageClient
import java.io.File
import java.io.FileOutputStream

class FileStorageClientImpl(private val context: Context) : FileStorageClient {
    override suspend fun savePlaylistCover(uriString: String): String? {
        return try {
            val resolver: ContentResolver = context.contentResolver
            val uri = Uri.parse(uriString)
            val inputStream = resolver.openInputStream(uri) ?: return null

            val fileName = "playlist_cover_${System.currentTimeMillis()}.jpg"
            val file = File(context.filesDir, fileName)
            val outputStream = FileOutputStream(file)

            inputStream.copyTo(outputStream)

            inputStream.close()
            outputStream.close()

            file.absolutePath
        } catch (e: Exception) {
            null
        }
    }
}