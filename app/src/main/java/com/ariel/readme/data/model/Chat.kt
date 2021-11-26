package com.ariel.readme.data.model

import com.google.firebase.firestore.DocumentId

data class Chat(
    @DocumentId val cid: String? = null,
    val participants: List<String> = emptyList(),
): ManagedModel()