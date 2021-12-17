package com.ariel.readme.controllers

import android.media.MediaRecorder
import java.io.File

class VoiceRecordingContext(val outputFile: File, val mediaRecorder: MediaRecorder) {
    fun start(){
        return mediaRecorder.start()
    }

    fun stop(){
        return mediaRecorder.stop()
    }
}