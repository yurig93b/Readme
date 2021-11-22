package com.ariel.readme.data.repo.interfaces

import com.google.firebase.firestore.DocumentReference

interface IGetDocRef: IGetFailure {
    fun onSuccess(d:DocumentReference?)
}