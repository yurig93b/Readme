package com.ariel.readme.data.repo.interfaces

import com.ariel.readme.data.model.Chat
import com.ariel.readme.data.model.Message
import com.ariel.readme.data.repo.ModeledChangedDocuments
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference

interface IMessageRepository {
    fun createMessage(msg: Message): Task<DocumentReference>
    fun getChatMessages(cid:String): Task<ModeledChangedDocuments<Message>>
    fun getChatMessages(chat: Chat): Task<ModeledChangedDocuments<Message>>
}