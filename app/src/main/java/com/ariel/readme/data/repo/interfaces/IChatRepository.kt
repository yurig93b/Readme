package com.ariel.readme.data.repo.interfaces

import com.ariel.readme.data.model.Chat
import com.ariel.readme.data.model.User
import com.ariel.readme.data.repo.ModeledChangedDocuments
import com.ariel.readme.data.repo.ModeledDocument
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot

interface IChatRepository {
    fun createChat(chat: Chat): Task<DocumentReference>
    fun getChat(cid: String): Task<ModeledDocument<Chat>>
    fun getChatsByUser(user: User): Task<ModeledChangedDocuments<Chat>>
    fun getChatsByUsers(participants:List<String>): Task<ModeledChangedDocuments<Chat>>
    fun listenOnChats(uid: String, listener: IGetChangedModels<Chat>): ListenerRegistration
}