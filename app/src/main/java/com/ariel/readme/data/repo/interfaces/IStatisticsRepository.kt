package com.ariel.readme.data.repo.interfaces

import com.ariel.readme.data.model.Statistic
import com.ariel.readme.data.model.User
import com.ariel.readme.data.repo.ModeledChangedDocuments
import com.ariel.readme.data.repo.ModeledDocument
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot

interface IStatisticsRepository {
    fun getStatisticsBetweenTimestamp(from: Timestamp, to :Timestamp):Task<ModeledChangedDocuments<Statistic>>
}