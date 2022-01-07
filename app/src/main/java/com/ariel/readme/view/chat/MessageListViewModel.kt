package com.ariel.readme.view.chat

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ariel.readme.data.model.Chat
import com.ariel.readme.data.model.Message
import com.ariel.readme.data.model.User
import com.ariel.readme.data.repo.ModeledDocument
import com.ariel.readme.data.repo.interfaces.IGetChangedModels
import com.ariel.readme.factories.RepositoryFactory
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.ListenerRegistration
import kotlin.streams.toList

class MessageListViewModel : ViewModel() {

    private var _listenRegistration: ListenerRegistration? = null

    private val _loading: MutableLiveData<Boolean> = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _metadataLoaded: MutableLiveData<Boolean> = MutableLiveData(false)
    val metadataLoaded: LiveData<Boolean> = _metadataLoaded

    private val _error: MutableLiveData<Exception> = MutableLiveData()
    val error: LiveData<Exception> = _error

    private val _chat: MutableLiveData<Chat> = MutableLiveData()

    private val _userInfo: MutableLiveData<MutableMap<String, User>> = MutableLiveData(
        mutableMapOf()
    )
    val userInfo: LiveData<MutableMap<String, User>> = _userInfo

    @RequiresApi(Build.VERSION_CODES.N)
    fun loadChatInfo(cid: String) {
        _loading.value = true

        RepositoryFactory.getChatRepository().getChat(cid).addOnSuccessListener { c ->
            _loading.value = true
            _chat.value = c.obj

            // Get user fetch tasks
            val userTasks = c.obj?.participants?.stream()
                ?.map { p -> RepositoryFactory.getUserRepository().getUserById(p) }?.toList()

            // When all user data is fetched populate this map
            Tasks.whenAllSuccess<ModeledDocument<User>>(userTasks)
                .addOnSuccessListener { tasks ->
                    tasks.forEach { user ->
                        _userInfo.value?.apply {
                            put(user.obj?.uid!!, user.obj)
                        }
                    }
                    _metadataLoaded.value = true
                }.addOnFailureListener { e -> _error.value = e }
                .addOnCompleteListener { _loading.value = false }
        }.addOnFailureListener { e -> _error.value = e }
            .addOnCompleteListener { _loading.value = false }
    }

    fun listenOnChatMessages(
        listener: IGetChangedModels<Message>
    ): ListenerRegistration {
        if (_listenRegistration == null) {
            return RepositoryFactory.getMessageRepository()
                .listenChatMessages(_chat.value?.cid!!, listener)
        }
        throw RuntimeException("Already registered.")
    }

    override fun onCleared() {
        _listenRegistration?.apply {
            remove()
            _listenRegistration = null
        }
    }
}