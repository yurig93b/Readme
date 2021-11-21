package com.ariel.readme.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class Message(
    @DocumentId val mid: String? = null,
    val cid: String,
    val from: String,
    val ts: Timestamp = Timestamp.now(),
    val text: String = "",
    val translation: String? = null,
    val is_voice: Boolean = false,
    val translation_status: TranslationStatus = TranslationStatus.PENDING
)