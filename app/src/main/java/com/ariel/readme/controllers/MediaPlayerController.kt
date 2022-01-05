package com.ariel.readme.controllers

import android.media.MediaPlayer

class MediaPlayerController {
    companion object{
        fun getPlayingContext(url: String): MediaPlayer {
            val m = MediaPlayer()
            m.setDataSource(url)
            m.prepare()
            return m
        }
    }
}