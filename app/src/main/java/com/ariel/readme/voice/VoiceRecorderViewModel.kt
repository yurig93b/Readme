package com.ariel.readme.voice

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ariel.readme.controllers.VoiceRecordingContext
import com.ariel.readme.controllers.VoiceRecordingController
import com.ariel.readme.data.model.Message
import com.ariel.readme.factories.RepositoryFactory
import com.ariel.readme.factories.StorageFactory
import com.ariel.readme.factories.StoragePathFactory

class VoiceRecorderViewModel : ViewModel() {
    private val _loading: MutableLiveData<Boolean> = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _recording: MutableLiveData<Boolean> = MutableLiveData(false)

    private val _error: MutableLiveData<Exception> = MutableLiveData()
    val error: LiveData<Exception> = _error

    private val _record_context: MutableLiveData<VoiceRecordingContext> = MutableLiveData()

    @RequiresApi(Build.VERSION_CODES.O)
    fun record(c: Context) {
        _recording.value = true
        _record_context.value = VoiceRecordingController(c).getRecordingContext()
        _record_context.value?.start()
    }

    fun stopRecording() {
        try {
            _record_context.value?.stop()

        } catch (e: java.lang.RuntimeException) {
            throw e
        } finally {
            _recording.value = false

        }
    }

    fun upload(chatId: String, from: String) {
        if (_recording.value == true || _loading.value == true) {
            throw RuntimeException("You can't upload while recording or uploading.")
        }
        _loading.value = true
        val msg = Message(cid = chatId, from = from, voice = true)

        // Create msg
        RepositoryFactory.getMessageRepository().createMessage(msg).addOnSuccessListener { dref ->
            val uri = Uri.parse("file://" + _record_context.value!!.outputFile.absolutePath)
            val storagePath = StoragePathFactory.getVoiceMessagePath(
                dref.id,
                VoiceRecordingController.DEFAULT_SUFFIX
            )

            // Upload file
            _loading.value = true
            StorageFactory.getStorage().putFile(storagePath, uri)
                .addOnCompleteListener {
                    _loading.value = false
                }.addOnFailureListener { e ->
                    _error.value = e
                }

        }.addOnCompleteListener {
            _loading.value = false
        }.addOnFailureListener { e ->
            _error.value = e
        }
    }


}