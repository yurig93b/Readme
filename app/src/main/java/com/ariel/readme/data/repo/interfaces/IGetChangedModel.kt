package com.ariel.readme.data.repo.interfaces

import com.google.firebase.firestore.DocumentSnapshot

interface IGetChangedModel<Model>: IGetFailure {
    fun onSuccess(d: Model?, raw: DocumentSnapshot?)
}