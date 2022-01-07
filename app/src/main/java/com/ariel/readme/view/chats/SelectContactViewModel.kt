package com.ariel.readme.view.chats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ariel.readme.data.model.Chat
import com.ariel.readme.data.repo.interfaces.IChatRepository
import com.ariel.readme.data.repo.interfaces.IUserRepository
import com.ariel.readme.factories.RepositoryFactory
import com.ariel.readme.services.AuthService.getCurrentFirebaseUser

class SelectContactViewModel : ViewModel() {
    val _selectedContactChatId: MutableLiveData<String> = MutableLiveData()
    val selectedContactChat: LiveData<String> = _selectedContactChatId
    private val _error: MutableLiveData<String> = MutableLiveData()
    val error: LiveData<String> = _error
    private val userRepo: IUserRepository = RepositoryFactory.getUserRepository()
    private val chatRepo: IChatRepository = RepositoryFactory.getChatRepository()

    fun ensureChat(contactNumber: String) {
        val sanitizedNumber = contactNumber.filter { c -> c.isDigit() || c == '+' }
        val myUid = getCurrentFirebaseUser()?.uid  //my uid(my number,..)
        userRepo.getUserByPhone(sanitizedNumber).addOnSuccessListener { users ->
            if (users.changes.isEmpty()) {
                _error.value = "Can't find target contact."
                return@addOnSuccessListener
            }

            val targetUser = users.changes[0].obj
            val participants = listOf(myUid!!, targetUser.uid!!)
            chatRepo.getChatsByUsers(participants).addOnSuccessListener { chats ->
                if (chats.changes.size > 0) {
                    _selectedContactChatId.value = chats.changes[0].obj.cid
                } else {
                    val newChat = Chat(null, participants)
                    chatRepo.createChat(newChat).addOnSuccessListener { createdChat ->
                        _selectedContactChatId.value = createdChat.id
                    }
                }
            }
        }
    }
}








