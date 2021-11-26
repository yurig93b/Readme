package com.ariel.readme.data.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

data class User(
    @DocumentId val uid: String? = null,
    val phone: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val manager: Boolean = false,
    var token: String? = null
): ManagedModel()