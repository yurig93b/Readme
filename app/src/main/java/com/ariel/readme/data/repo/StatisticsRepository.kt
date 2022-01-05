package com.ariel.readme.data.repo

import com.ariel.readme.data.model.Statistic
import com.ariel.readme.data.model.User
import com.ariel.readme.data.repo.interfaces.IGetChangedModel
import com.ariel.readme.data.repo.interfaces.IGetChangedModels
import com.ariel.readme.data.repo.interfaces.IStatisticsRepository
import com.ariel.readme.data.repo.interfaces.IUserRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*

class StatisticsRepository : FirebaseRepository<Statistic>(), IStatisticsRepository {
    override val rootNode: String
        get() = "statistics"

    override fun getStatisticsBetweenTimestamp(
        from: Timestamp,
        to: Timestamp
    ): Task<ModeledChangedDocuments<Statistic>> {
        return HookQuery(collRef.whereGreaterThanOrEqualTo(Statistic::ts.name, from).whereLessThanOrEqualTo(Statistic::ts.name, to).get())
    }
}

