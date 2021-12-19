package com.ariel.readme.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp

data class Message(
    @DocumentId val mid: String? = null,
    val cid: String? = null,
    val from: String? = null,
    @ServerTimestamp
    val ts: Timestamp? = null,
    val text: String = "",
    val translation: String? = null,
    val voice: Boolean = false,
    val translationStatus: JobStatus = JobStatus.PENDING,
    val transcriptStatus: JobStatus = JobStatus.PENDING
): ManagedModel()