package com.example.playlistmaker.service

import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.player.models.PlayerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class PlayerService : PlayerController, Service() {

    private var currentTrackTitle: String = ""

    private val binder = PlayerBinder()
    private val mediaPlayer = MediaPlayer()
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private var timerJob: Job? = null
    private val _playerState = MutableStateFlow(PlayerState.DEFAULT)
    override val playerState: StateFlow<PlayerState> = _playerState

    private val _currentTime = MutableStateFlow(0)
    override val currentTime: StateFlow<Int> = _currentTime


    override fun onBind(intent: Intent): IBinder {

        val artist = intent.getStringExtra("EXTRA_ARTIST").orEmpty()
        val title = intent.getStringExtra("EXTRA_TITLE").orEmpty()
        currentTrackTitle = "$artist - $title"
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }


    inner class PlayerBinder : Binder() {
        fun getService(): PlayerService = this@PlayerService
    }

    override fun prepareAndPlay(url: String) {
        if (url.isBlank()) return


        mediaPlayer.reset()
        mediaPlayer.setDataSource(url)
        mediaPlayer.setOnPreparedListener {
            _playerState.value = PlayerState.PREPARED
            play()

        }
        mediaPlayer.setOnCompletionListener {
            stopTimer()
            _currentTime.value = 0
            _playerState.value = PlayerState.DEFAULT
            stopForegroundMode()
        }
        mediaPlayer.prepareAsync()

    }

    override fun play() {
        if (_playerState.value == PlayerState.PLAYING) return

        mediaPlayer.start()
        _playerState.value = PlayerState.PLAYING
        startTimer()

    }

    override fun pause() {
        if (_playerState.value != PlayerState.PLAYING) return
        mediaPlayer.pause()
        _playerState.value = PlayerState.PAUSED
        stopTimer()
        stopForegroundMode()


    }

    private fun startTimer() {
        stopTimer()
        timerJob = serviceScope.launch {
            while (isActive && mediaPlayer.isPlaying) {
                _currentTime.value = mediaPlayer.currentPosition
                delay(300)
            }

        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    override fun startForegroundMode() {
        startForeground(1, createNotification())
    }

    override fun stopForegroundMode() {
        stopForeground(STOP_FOREGROUND_REMOVE)
    }

    override fun initTrack(artist: String, title: String) {
        currentTrackTitle = "$artist - $title"
    }

    private fun createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = android.app.NotificationChannel(
                "player_channel",
                "Playback",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, "player_channel")
            .setSmallIcon(R.drawable.ic_play_100)
            .setContentTitle("Playlist Maker")
            .setContentText(if (currentTrackTitle.isNotEmpty()) currentTrackTitle else "Loading...")
            .setOnlyAlertOnce(true)
            .build()
    }


    override fun onDestroy() {
        stopTimer()
        mediaPlayer.release()
        serviceScope.cancel()
        super.onDestroy()
    }


}
