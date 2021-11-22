package com.ariel.readme.data.repo

import com.ariel.readme.data.model.User
import com.ariel.readme.data.repo.interfaces.IGetChangedModel
import com.ariel.readme.data.repo.interfaces.IGetChangedModels
import com.ariel.readme.data.repo.interfaces.IGetDocRef
import com.ariel.readme.data.repo.interfaces.IGetModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*

class UserRepository : FirebaseRepository<User>() {
    override val rootNode: String
        get() = "users"

    fun getUserByPhone(phone: String, listener: IGetChangedModels<User>): Task<QuerySnapshot> {
        return HookQuery(collRef.whereEqualTo(User::phone.name, phone).get(), listener)
    }

    fun getAllManagers(listener: IGetChangedModels<User>): Task<QuerySnapshot> {
        return HookQuery(collRef.whereEqualTo(User::manager.name, true).get(), listener)
    }

    fun getCurrentUser(user: FirebaseUser, listener: IGetModel<User>): Task<DocumentSnapshot> {
        return HookGetDocumentSnapshot(collRef.document(user.uid).get(), listener)
    }

    fun listenOnUsersChanges(listener: IGetChangedModels<User>): ListenerRegistration {
        return HookListenCollection(collRef, listener)
    }

    fun listenOnUserChanges(uid: String, listener: IGetChangedModel<User>): ListenerRegistration {
        return HookDocumentListen(collRef.document(uid), listener)
    }

    fun listenOnUserChanges(user: User, listener: IGetChangedModel<User>): ListenerRegistration {
        return listenOnUserChanges(user.uid!!, listener)
    }

    fun registerUser(user:User, listener: IGetDocRef): Task<out Any> {
        return HookDocumentAddOrSet(collRef.document(user.uid!!).set(user), listener)
    }

}

