package com.ariel.readme.data.model

import com.google.firebase.firestore.DocumentId

abstract class ManagedModel{
    init {
        validate()
    }

    protected open fun validate(){
        // Default empty impl
    }
}