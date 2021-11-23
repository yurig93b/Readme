package com.ariel.readme.data.repo

import com.ariel.readme.data.model.Chat
import com.ariel.readme.data.model.Message
import com.ariel.readme.data.repo.interfaces.IGetChangedModels
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot

class MessageRepository : FirebaseRepository<Chat>() {
    override val rootNode: String
        get() = "messages"

    fun createMessage(msg: Message): Task<out Any> {
        return collRef.add(msg)
    }

    fun getChatMessages(cid:String): Task<QuerySnapshot> {
        return collRef.whereEqualTo(Message::cid.name, cid).get()
    }

    fun getChatMessages(chat: Chat): Task<QuerySnapshot> {
        return getChatMessages(chat.cid!!)
    }

}

