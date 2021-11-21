package com.ariel.readme.data.model

import com.google.firebase.firestore.DocumentId

data class User(
    @DocumentId val uid: String? = null,
    val phone: String? = null,
    val first_name: String? = null,
    val last_name: String? = null
)