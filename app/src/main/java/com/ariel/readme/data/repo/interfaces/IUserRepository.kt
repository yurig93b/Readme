package com.ariel.readme.data.repo.interfaces

import com.ariel.readme.data.model.User
import com.ariel.readme.data.repo.ModeledChangedDocuments
import com.ariel.readme.data.repo.ModeledDocument
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot

interface IUserRepository {
    fun getUserByPhone(phone: String):Task<ModeledChangedDocuments<User>>
    fun getAllManagers(): Task<ModeledChangedDocuments<User>>
    fun getUserById(uid: String): Task<ModeledDocument<User>>
    fun getCurrentUser(user: FirebaseUser): Task<ModeledDocument<User>>
    fun listenOnUsersChanges(listener: IGetChangedModels<User>): ListenerRegistration
    fun listenOnUserChanges(uid: String, listener: IGetChangedModel<User>): ListenerRegistration
    fun listenOnUserChanges(user: User, listener: IGetChangedModel<User>): ListenerRegistration
    fun registerUser(user: User): Task<Void>
}