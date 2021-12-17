package com.ariel.readme.data.repo

import android.widget.Toast
import com.ariel.readme.data.model.HotWord
import com.ariel.readme.data.model.User
import com.ariel.readme.data.repo.interfaces.IGetChangedModels
import com.ariel.readme.data.repo.interfaces.IHotWordRepository
import com.ariel.readme.hotWords
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase
import com.squareup.okhttp.Dispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception

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

    fun addHotWord(hotword:HotWord) {
        //collRef.whereEqualTo(HotWord::uid.name, FirebaseAuth.getInstance().currentUser!!.uid)
        collRef.whereEqualTo(HotWord::uid.name, "1234")
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

    fun removeHotWord(word: String){
        //collRef.whereEqualTo(HotWord::uid.name, FirebaseAuth.getInstance().currentUser!!.uid)
        collRef.whereEqualTo(HotWord::uid.name, "1234")
            .whereEqualTo(HotWord::word.name, word)
            .get().addOnSuccessListener {
                res -> for(doc in res.documents){
                    try{
                        collRef.document(doc.id).delete()
                    }
                    catch(e: Exception){ }
                }
            }
    }

    fun clearHotWords(){
        //collRef.whereEqualTo(HotWord::uid.name, FirebaseAuth.getInstance().currentUser!!.uid).get().await()
        collRef.whereEqualTo(HotWord::uid.name, "1234").get().addOnSuccessListener {
            res -> for(doc in res.documents){
                try{
                    collRef.document(doc.id).delete()
                }
                catch(e: Exception){ }
            }
        }
    }

}

