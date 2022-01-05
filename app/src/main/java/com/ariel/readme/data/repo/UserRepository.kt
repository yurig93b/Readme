package com.ariel.readme.data.repo

import com.ariel.readme.data.model.User
import com.ariel.readme.data.repo.interfaces.IGetChangedModel
import com.ariel.readme.data.repo.interfaces.IGetChangedModels
import com.ariel.readme.data.repo.interfaces.IUserRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*

class UserRepository : FirebaseRepository<User>(), IUserRepository {
    override val rootNode: String
        get() = "users"

    override fun getUserByPhone(phone: String):Task<ModeledChangedDocuments<User>> {
        return HookQuery(collRef.whereEqualTo(User::phone.name, phone).get())
    }

    override fun getAllManagers(): Task<ModeledChangedDocuments<User>> {
        return HookQuery(collRef.whereEqualTo(User::manager.name, true).get())
    }

    override fun getUserById(uid: String): Task<ModeledDocument<User>> {
        return HookGetDocumentSnapshot(collRef.document(uid).get())
    }

    override fun getCurrentUser(user: FirebaseUser): Task<ModeledDocument<User>> {
        return HookGetDocumentSnapshot(collRef.document(user.uid).get())
    }

    override fun listenOnUsersChanges(listener: IGetChangedModels<User>): ListenerRegistration {
        return HookListenCollection(collRef, listener)
    }

    override fun listenOnUserChanges(uid: String, listener: IGetChangedModel<User>): ListenerRegistration {
        return HookListenDocument(collRef.document(uid), listener)
    }

    override fun listenOnUserChanges(user: User, listener: IGetChangedModel<User>): ListenerRegistration {
        return listenOnUserChanges(user.uid!!, listener)
    }

    override fun registerUser(user:User): Task<Void> {//מקבל אופיקט של יוזר
        return collRef.document(user.uid!!).set(user)
    }
}

