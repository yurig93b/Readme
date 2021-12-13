package com.ariel.readme.data.repo

import android.widget.Toast
import com.ariel.readme.data.model.HotWord
import com.ariel.readme.data.model.User
import com.ariel.readme.data.repo.interfaces.IGetChangedModels
import com.ariel.readme.data.repo.interfaces.IHotWordRepository
import com.ariel.readme.hotWords
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
        TODO("may be replaced by addHotWord")
    }

    fun addHotWord(hotword:HotWord) = CoroutineScope(Dispatchers.IO).launch {
        //val hotWordColl = collRef.whereEqualTo(HotWord::uid.name, FirebaseAuth.getInstance().currentUser!!.uid)
        val hotWordColl = collRef.whereEqualTo(HotWord::uid.name, "1234")
            .whereEqualTo(HotWord::word.name, hotword.word)
            .get().await()
        if(hotWordColl.documents.isEmpty()){
            collRef.add(hotword)
        }
    }

    override fun listenOnHotWords(uid: String,listener: IGetChangedModels<HotWord>): ListenerRegistration {
        return HookListenQuery(collRef.whereEqualTo(HotWord::uid.name, uid), listener)
    }

    override fun listenOnHotWords(user: User, listener: IGetChangedModels<HotWord>): ListenerRegistration {
        return listenOnHotWords(user.uid!!, listener)
    }

    fun removeHotWord(word: String) = CoroutineScope(Dispatchers.IO).launch{
        //val hotWordColl = collRef.whereEqualTo(HotWord::uid.name, FirebaseAuth.getInstance().currentUser!!.uid)
        val hotWordColl = collRef.whereEqualTo(HotWord::uid.name, "1234")
            .whereEqualTo(HotWord::word.name, word)
            .get().await()
        if(hotWordColl.documents.isNotEmpty()){
            for(doc in hotWordColl){
                try{
                    collRef.document(doc.id).delete().await()
                }
                catch(e: Exception){ }
            }
        }
    }

    fun clearHotWords() = CoroutineScope(Dispatchers.IO).launch{
        //val hotWordColl = collRef.whereEqualTo(HotWord::uid.name, FirebaseAuth.getInstance().currentUser!!.uid).get().await()
        val hotWordColl = collRef.whereEqualTo(HotWord::uid.name, "1234").get().await()
        if(hotWordColl.documents.isNotEmpty()){
            for(doc in hotWordColl){
                try{
                    collRef.document(doc.id).delete().await()
                }
                catch(e: Exception){ }
            }
        }
    }

}

