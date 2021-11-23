package com.ariel.readme.data.repo

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.firestore.DocumentChange
import java.util.*
import java.util.stream.Collectors
import kotlin.streams.toList

class ModeledDocumentChange<Model>
    (val rawDocumentChange: DocumentChange, val obj: Model, val changeType: DocumentChange.Type) {
        companion object {
            inline fun <reified Model> fromDocumentChange(change: DocumentChange): ModeledDocumentChange<Model> {
                return ModeledDocumentChange(change, change.document.toObject(Model::class.java),change.type)
            }

            @RequiresApi(Build.VERSION_CODES.N)
            inline fun <reified Model> fromDocumentChanges(changes: List<DocumentChange>?): List<ModeledDocumentChange<Model>> {
                if(changes == null){ return emptyList() }
                return changes.stream().map { v -> fromDocumentChange<Model>(v) }.toList()
            }
        }
}
