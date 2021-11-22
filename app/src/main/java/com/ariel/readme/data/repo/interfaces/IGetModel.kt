package com.ariel.readme.data.repo.interfaces

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot

interface IGetModel<Model> : IGetFailure {
    fun onSuccess(d: Model?, raw: DocumentSnapshot?, docRef: DocumentReference?)
}