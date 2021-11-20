package com.ariel.readme.data.repo

import com.google.firebase.database.DatabaseError

import com.google.firebase.database.DataSnapshot

import com.ariel.readme.data.mappers.FirebaseMapper

import com.google.firebase.database.ValueEventListener


class BaseValueEventListener<Entity, Model>(
    private val mapper: FirebaseMapper<Entity, Model>,
    callback: FirebaseRepository.FirebaseDatabaseRepositoryCallback<Model>
) : ValueEventListener {

    private val callback: FirebaseRepository.FirebaseDatabaseRepositoryCallback<Model>

    override fun onDataChange(dataSnapshot: DataSnapshot) {
        val data = mapper.mapList(dataSnapshot)
        callback.onSuccess(data)
    }

    override fun onCancelled(databaseError: DatabaseError) {
        callback.onError(databaseError.toException())
    }

    init {
        this.callback = callback
    }
}