package com.ariel.readme.data.repo

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import java.util.*
import java.util.stream.Collectors
import kotlin.streams.toList

class ModeledChangedDocuments<Model>
    (val changes: List<ModeledDocumentChange<Model>>, val raw: QuerySnapshot?) {
}
