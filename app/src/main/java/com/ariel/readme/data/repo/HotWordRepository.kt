package com.ariel.readme.data.repo

import com.ariel.readme.data.model.HotWord
import com.ariel.readme.data.model.User
import com.ariel.readme.data.repo.interfaces.IGetChangedModels
import com.ariel.readme.data.repo.interfaces.IHotWordRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*

class HotWordRepository : FirebaseRepository<HotWord>(), IHotWordRepository {
    override val rootNode: String
        get() = "hotwords"

    override fun getHotWords(uid: String): Task<ModeledChangedDocuments<HotWord>> {
        return HookQuery( collRef.whereEqualTo(HotWord::uid.name, uid).get())
    }

    override fun getHotWords(user: User): Task<ModeledChangedDocuments<HotWord>> {
        return getHotWords(user.uid!!)
    }

    override fun createHotWord(hotword:HotWord): Task<DocumentReference> {
        return collRef.add(hotword)
    }

    override fun listenOnHotWords(uid: String,listener: IGetChangedModels<HotWord>): ListenerRegistration {
        return HookListenQuery(collRef.whereEqualTo(HotWord::uid.name, uid), listener)
    }

    override fun listenOnHotWords(user: User, listener: IGetChangedModels<HotWord>): ListenerRegistration {
        return listenOnHotWords(user.uid!!, listener)
    }

}

