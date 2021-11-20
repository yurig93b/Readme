package com.ariel.readme.data.repo

import com.google.firebase.database.FirebaseDatabase

import com.ariel.readme.data.mappers.FirebaseMapper

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import java.lang.Exception


abstract class FirebaseRepository<Entity, Model>(mapper: FirebaseMapper<Entity, Model>) {
    protected var databaseReference: DatabaseReference
    protected var firebaseCallback: FirebaseDatabaseRepositoryCallback<Model>? = null
    private var listener: BaseValueEventListener<Entity, Model>? = null
    private val mapper: FirebaseMapper<Entity, Model>
    protected abstract val rootNode: String?

    fun addListener(firebaseCallback: FirebaseDatabaseRepositoryCallback<Model>) {
        this.firebaseCallback = firebaseCallback
        listener = BaseValueEventListener(mapper, firebaseCallback)
        databaseReference.addValueEventListener(listener!!)
    }

    fun removeListener() {
        databaseReference.removeEventListener(listener as ValueEventListener)
    }

    interface FirebaseDatabaseRepositoryCallback<T> {
        fun onSuccess(result: List<T>?)
        fun onError(e: Exception?)
    }

    init {
        databaseReference = FirebaseDatabase.getInstance().getReference(rootNode!!)
        this.mapper = mapper
    }
}