package com.ariel.readme.data.repo

import com.ariel.readme.data.model.Chat
import com.ariel.readme.data.model.Message
import com.ariel.readme.data.repo.interfaces.IGetChangedModels
import com.ariel.readme.data.repo.interfaces.IMessageRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query


class MessageRepository : FirebaseRepository<Chat>(), IMessageRepository {
    override val rootNode: String
        get() = "messages"

    override fun createMessage(msg: Message): Task<DocumentReference> {
        return collRef.add(msg)
    }

    override fun listenChatMessages(cid:String, listener: IGetChangedModels<Message>): ListenerRegistration {
        return HookListenQuery(collRef.whereEqualTo(Message::cid.name, cid).orderBy(Message::ts.name, Query.Direction.ASCENDING), listener)
    }

    override fun getChatMessages(cid:String): Task<ModeledChangedDocuments<Message>> {
        return HookQuery(collRef.whereEqualTo(Message::cid.name, cid).get())
    }

    override fun getChatMessages(chat: Chat): Task<ModeledChangedDocuments<Message>> {
        return getChatMessages(chat.cid!!)
    }

    fun getChatMessagesByTime(time: Timestamp): Task<ModeledChangedDocuments<Message>>{
        return HookQuery(collRef.whereGreaterThan(Message::ts.name, time).get())
    }

}

