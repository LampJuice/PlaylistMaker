package com.example.playlistmaker.data.storage

import android.content.Context
import android.net.Uri
import com.example.playlistmaker.data.FileStorageClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class FileStorageClientImpl(private val context: Context) : FileStorageClient {
    override suspend fun savePlaylistCover(uriString: String): String? =
        withContext(Dispatchers.IO){
            val resolver = context.contentResolver
            val uri = Uri.parse(uriString)

            val inputStream = resolver.openInputStream(uri) ?: return@withContext null

            val fileName = "playlist_cover_${System.currentTimeMillis()}.jpg"
            val file = File(context.filesDir,fileName)

            inputStream.use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }
            file.absolutePath
        }
    }
