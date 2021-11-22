package com.ariel.readme.data.repo

import com.ariel.readme.data.model.Chat
import com.ariel.readme.data.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*

class ChatRepository : FirebaseRepository<Chat>() {
    override val rootNode: String
        get() = "chats"

    fun createChat(chat:Chat): Task<DocumentReference> {
        return collRef.add(chat)
    }

    fun getChat(cid: String): Task<DocumentSnapshot> {
        return collRef.document(cid).get()
    }

    fun getChatsByUser(user:User): Task<QuerySnapshot> {
        return collRef.whereArrayContains(Chat::participants.name, user.uid!!).get()
    }

    fun listenOnChats(user: User, listener: EventListener<QuerySnapshot>): ListenerRegistration {
        return collRef.whereArrayContains(Chat::participants.name, user.uid!!).addSnapshotListener(listener)
    }

}

