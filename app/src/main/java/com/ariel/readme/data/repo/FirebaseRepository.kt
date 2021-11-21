package com.ariel.readme.data.repo


import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


abstract class FirebaseRepository<Model> {
    val collectionReference: CollectionReference
    protected abstract val rootNode: String

    init {
        collectionReference = Firebase.firestore.collection(rootNode)
    }
}