package com.example.playlistmaker.ui.player.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.playlistmaker.R

class PlaybackButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    enum class State {
        PLAY, PAUSE
    }

    private var state = State.PLAY

    private var playDrawable: Drawable? = null
    private var pauseDrawable: Drawable? = null


    var onClick: (() -> Unit)? = null

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PlaybackButtonView,
            defStyleAttr,
            0
        ).apply {
            try {
                val playRes = getResourceId(
                    R.styleable.PlaybackButtonView_playIcon, 0
                )
                val pauseRes = getResourceId(
                    R.styleable.PlaybackButtonView_pauseIcon, 0
                )

                if (playRes != 0) {
                    playDrawable = context.getDrawable(playRes)
                }
                if (pauseRes != 0) {
                    pauseDrawable = context.getDrawable(pauseRes)
                }
            } finally {
                recycle()
            }

        }

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        playDrawable?.setBounds(0, 0, w, h)
        pauseDrawable?.setBounds(0, 0, w, h)

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        when (state) {
            State.PLAY -> playDrawable
            State.PAUSE -> pauseDrawable
        }?.draw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> return true
            MotionEvent.ACTION_UP -> {
                onClick?.invoke()
                return true
            }
        }
        return super.onTouchEvent(event)
    }


    fun setPlaying(isPlaying: Boolean) {
        state = if (isPlaying) State.PAUSE else State.PLAY
        invalidate()
    }


}


