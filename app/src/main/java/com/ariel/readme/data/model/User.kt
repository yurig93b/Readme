package com.ariel.readme.data.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

data class User(
    @DocumentId val uid: String? = null,
    val phone: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val banned: Boolean = false,
    val manager: Boolean = false,
    val token: String? = null
): ManagedModel()
