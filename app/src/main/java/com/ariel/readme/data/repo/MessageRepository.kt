package com.ariel.readme.data.repo

import com.ariel.readme.data.model.Chat
import com.ariel.readme.data.model.Message
import com.ariel.readme.data.repo.interfaces.IGetChangedModels
import com.ariel.readme.data.repo.interfaces.IGetDocRef
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

class MessageRepository : FirebaseRepository<Chat>() {
    override val rootNode: String
        get() = "messages"

    fun createMessage(msg: Message, listener: IGetDocRef): Task<out Any> {
        return HookDocumentAddOrSet(collRef.add(msg), listener)
    }

    fun getChatMessages(cid:String, listener: IGetChangedModels<Chat>): Task<QuerySnapshot> {
        return HookQuery(collRef.whereEqualTo(Message::cid.name, cid).get(), listener)
    }

    fun getChatMessages(chat: Chat, listener: IGetChangedModels<Chat>): Task<QuerySnapshot> {
        return getChatMessages(chat.cid!!, listener)
    }

}

