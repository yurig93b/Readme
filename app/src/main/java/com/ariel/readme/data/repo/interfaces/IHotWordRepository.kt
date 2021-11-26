package com.ariel.readme.data.repo.interfaces

import com.ariel.readme.data.model.HotWord
import com.ariel.readme.data.model.User
import com.ariel.readme.data.repo.ModeledChangedDocuments
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ListenerRegistration

interface IHotWordRepository {
    fun getHotWords(uid: String): Task<ModeledChangedDocuments<HotWord>>
    fun getHotWords(user: User): Task<ModeledChangedDocuments<HotWord>>
    fun createHotWord(hotword: HotWord): Task<DocumentReference>
    fun listenOnHotWords(uid: String,listener: IGetChangedModels<HotWord>): ListenerRegistration
    fun listenOnHotWords(user: User, listener: IGetChangedModels<HotWord>): ListenerRegistration
}