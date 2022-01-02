package com.ariel.readme.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

class statistics(
    @DocumentId val ts: Timestamp? = null,
    val mpr: Int = 0,
    val active_users: Int = 0): ManagedModel()