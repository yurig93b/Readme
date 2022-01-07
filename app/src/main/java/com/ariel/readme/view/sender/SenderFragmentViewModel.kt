package com.ariel.readme.view.sender

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

class SenderFragmentViewModel : ViewModel() {
    private val _loading_voice: MutableLiveData<Boolean> = MutableLiveData(false)
    val loading_voice: LiveData<Boolean> = _loading_voice

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

    fun sendMessage(chatId: String, from: String, msg: String) {
        val msgObj = Message(cid = chatId, from = from, voice = false, text = msg)
        RepositoryFactory.getMessageRepository().createMessage(msgObj).addOnFailureListener { e ->
            _error.value = e
        }
    }

    fun upload(chatId: String, from: String) {
        if (_recording.value == true || _loading_voice.value == true) {
            throw RuntimeException("You can't upload while recording or uploading.")
        }
        _loading_voice.value = true
        val msg = Message(cid = chatId, from = from, voice = true)

        // Create msg
        RepositoryFactory.getMessageRepository().createMessage(msg).addOnSuccessListener { dref ->
            val uri = Uri.parse("file://" + _record_context.value!!.outputFile.absolutePath)
            val storagePath = StoragePathFactory.getVoiceMessagePath(
                dref.id,
                VoiceRecordingController.DEFAULT_SUFFIX
            )

            // Upload file
            _loading_voice.value = true
            StorageFactory.getStorage().putFile(storagePath, uri)
                .addOnCompleteListener {
                    _loading_voice.value = false
                }.addOnFailureListener { e ->
                    _error.value = e
                }

        }.addOnCompleteListener {
            _loading_voice.value = false
        }.addOnFailureListener { e ->
            _error.value = e
        }
    }


}