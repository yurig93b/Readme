package com.ariel.readme.data.repo

import com.ariel.readme.data.model.HotWord
import com.ariel.readme.data.model.User
import com.ariel.readme.data.repo.interfaces.IGetChangedModels
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*

class HotWordRepository : FirebaseRepository<HotWord>() {
    override val rootNode: String
        get() = "hotwords"

    fun getHotWords(uid: String): Task<QuerySnapshot> {
        return collRef.whereEqualTo(HotWord::uid.name, uid).get()
    }

    fun getHotWords(user: User): Task<QuerySnapshot> {
        return getHotWords(user.uid!!)
    }

    fun createHotWord(hotword:HotWord): Task<DocumentReference> {
        return collRef.add(hotword)
    }

    fun listenOnHotWords(uid: String,listener: IGetChangedModels<HotWord>): ListenerRegistration {
        return HookListenQuery(collRef.whereEqualTo(HotWord::uid.name, uid), listener)
    }

    fun listenOnHotWords(user: User, listener: IGetChangedModels<HotWord>): ListenerRegistration {
        return listenOnHotWords(user.uid!!, listener)
    }

}

