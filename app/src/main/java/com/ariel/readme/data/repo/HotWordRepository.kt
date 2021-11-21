package com.ariel.readme.data.repo

import com.ariel.readme.data.model.Chat
import com.ariel.readme.data.model.HotWord
import com.ariel.readme.data.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*

class HotWordRepository : FirebaseRepository<Chat>() {
    override val rootNode: String
        get() = "hotwords"

    fun getHotWords(uid: String): Query {
       return collectionReference.whereEqualTo(HotWord::uid.name, uid)
    }

    fun getHotWords(user: User): Query {
        return getHotWords(user.uid!!)
    }

    fun createHotWord(chat:Chat): Task<DocumentReference> {
        return collectionReference.add(chat)
    }


    fun listenOnHotWords(uid: String, listener: EventListener<QuerySnapshot>): ListenerRegistration {
        return collectionReference.whereEqualTo(HotWord::uid.name, uid).addSnapshotListener(listener)
    }

    fun listenOnHotWords(user: User, listener: EventListener<QuerySnapshot>): ListenerRegistration {
        return listenOnHotWords(user.uid!!, listener)
    }

}

