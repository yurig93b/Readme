package com.ariel.readme.data.repo

import com.ariel.readme.data.model.User
import com.ariel.readme.data.repo.interfaces.IGetChangedModel
import com.ariel.readme.data.repo.interfaces.IGetChangedModels
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*

class UserRepository : FirebaseRepository<User>() {
    override val rootNode: String
        get() = "users"

    fun getUserByPhone(phone: String): Task<QuerySnapshot> {
        return collRef.whereEqualTo(User::phone.name, phone).get()
    }

    fun getAllManagers(): Task<ModeledChangedDocuments<User>> {
        return HookQuery(collRef.whereEqualTo(User::manager.name, true).get())
    }

    fun getCurrentUser(user: FirebaseUser): Task<ModeledDocument<User>> {
        return HookGetDocumentSnapshot(collRef.document(user.uid).get())
    }

    fun listenOnUsersChanges(listener: IGetChangedModels<User>): ListenerRegistration {
        return HookListenCollection(collRef, listener)
    }

    fun listenOnUserChanges(uid: String, listener: IGetChangedModel<User>): ListenerRegistration {
        return HookListenDocument(collRef.document(uid), listener)
    }

    fun listenOnUserChanges(user: User, listener: IGetChangedModel<User>): ListenerRegistration {
        return listenOnUserChanges(user.uid!!, listener)
    }

    fun registerUser(user:User): Task<Void> {
        return collRef.document(user.uid!!).set(user)
    }

}

