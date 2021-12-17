package com.ariel.readme.controllers

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.io.path.createTempFile as createTempFile

class VoiceRecordingController(private val context: Context) {
    private var LOG_TAG = "RECORDER";

    companion object{
        val DEFAULT_OUTPUT_FORMAT = MediaRecorder.OutputFormat.AMR_WB
        val DEFAULT_ENCODER = MediaRecorder.AudioEncoder.AMR_WB
        val DEFAULT_SUFFIX = ".amr"
    }

    private fun genRecordingPath(): File {
        return createTempFile(UUID.randomUUID().toString(), DEFAULT_SUFFIX, context.externalCacheDir)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getRecordingContext(): VoiceRecordingContext{
        val tempFile = genRecordingPath()

        val recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(DEFAULT_OUTPUT_FORMAT)
            setAudioEncoder(DEFAULT_ENCODER)
            setOutputFile(tempFile)

            try {
                prepare()
            } catch (e: IOException) {
                Log.e(LOG_TAG, "prepare() failed")
            }
        }

        return VoiceRecordingContext(tempFile, recorder)
    }
}