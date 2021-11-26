package com.ariel.readme.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

data class Message(
    @DocumentId val mid: String? = null,
    val cid: String,
    val from: String,
    val ts: Timestamp = Timestamp.now(),
    val text: String = "",
    val translation: String? = null,
    val voice: Boolean = false,
    val translationStatus: TranslationStatus = TranslationStatus.PENDING
): ManagedModel()