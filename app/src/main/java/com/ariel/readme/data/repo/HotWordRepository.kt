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

    override fun createHotWord(hotword: HotWord): Task<DocumentReference> {
        return collRef.add(hotword)
    }

    fun addHotWord(hotword:HotWord, uid: String): Task<QuerySnapshot> {
        return collRef.whereEqualTo(HotWord::uid.name, uid)
            .whereEqualTo(HotWord::word.name, hotword.word)
            .get().addOnSuccessListener { res -> if(res.isEmpty){
                collRef.add(hotword)
                }
            }
    }

    override fun listenOnHotWords(uid: String,listener: IGetChangedModels<HotWord>): ListenerRegistration {
        return HookListenQuery(collRef.whereEqualTo(HotWord::uid.name, uid), listener)
    }

    override fun listenOnHotWords(user: User, listener: IGetChangedModels<HotWord>): ListenerRegistration {
        return listenOnHotWords(user.uid!!, listener)
    }

    fun removeHotWord(word: String, uid: String): Task<QuerySnapshot> {
        return collRef.whereEqualTo(HotWord::uid.name, uid)
            .whereEqualTo(HotWord::word.name, word)
            .get().addOnSuccessListener {
                res -> for(doc in res.documents){
                    collRef.document(doc.id).delete()
                }
            }
    }

    fun clearHotWords(uid: String): Task<QuerySnapshot> {
        return collRef.whereEqualTo(HotWord::uid.name, uid).get().addOnSuccessListener {
            res -> for(doc in res.documents){
                collRef.document(doc.id).delete()
            }
        }
    }

}

