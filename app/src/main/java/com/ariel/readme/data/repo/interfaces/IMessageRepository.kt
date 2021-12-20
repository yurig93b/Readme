package com.ariel.readme.data.repo.interfaces

import com.ariel.readme.data.model.Chat
import com.ariel.readme.data.model.Message
import com.ariel.readme.data.model.User
import com.ariel.readme.data.repo.ModeledChangedDocuments
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ListenerRegistration

interface IMessageRepository {
    fun createMessage(msg: Message): Task<DocumentReference>
    fun listenChatMessages(cid: String, listener: IGetChangedModels<Message>): ListenerRegistration
    fun getChatMessages(cid: String): Task<ModeledChangedDocuments<Message>>
    fun getChatMessages(chat: Chat): Task<ModeledChangedDocuments<Message>>
}