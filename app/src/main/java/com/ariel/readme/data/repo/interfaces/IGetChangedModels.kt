package com.ariel.readme.data.repo.interfaces

import com.ariel.readme.data.repo.ModeledDocumentChange
import com.google.firebase.firestore.QuerySnapshot

interface IGetChangedModels<Model>: IGetFailure {
    fun onSuccess(d: List<ModeledDocumentChange<Model>>, raw: QuerySnapshot?)
}