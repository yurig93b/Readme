package com.ariel.readme.data.repo

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.database.FirebaseDatabase


import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.Exception


abstract class FirebaseRepository<Model> {
    val collectionReference: CollectionReference
    protected abstract val rootNode: String

    init {
        collectionReference = Firebase.firestore.collection(rootNode)
    }
}