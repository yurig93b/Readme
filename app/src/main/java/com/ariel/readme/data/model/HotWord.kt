package com.ariel.readme.data.model

import com.google.firebase.firestore.DocumentId

data class HotWord(
    @DocumentId val hwid: String? = null,
    val uid: String?  = null,
    val word: String = "error!",
): ManagedModel()