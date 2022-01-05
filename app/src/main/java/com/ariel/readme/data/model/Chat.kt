package com.ariel.readme.data.model

import com.google.firebase.firestore.DocumentId

data class Chat(
    @DocumentId var cid: String? = null,
    val participants: List<String> = emptyList(),
    var participantsStr: String? = null,
): ManagedModel()