package com.ariel.readme.data.repo

import com.ariel.readme.data.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*

class UserRepository : FirebaseRepository<User>() {
    override val rootNode: String
        get() = "users"

    fun getUsersByFirstName(name: String): Task<QuerySnapshot> {
        return collectionReference.whereEqualTo(User::first_name.name, name).get()
    }

    fun getCurrentUser(user: FirebaseUser): Task<DocumentSnapshot> {
        return collectionReference.document(user.uid).get()
    }

    fun listenOnUsersChanges(listener: EventListener<QuerySnapshot>): ListenerRegistration {
        return collectionReference.addSnapshotListener(listener)
    }

    fun listenOnUserChanges(uid: String, listener: EventListener<DocumentSnapshot>): ListenerRegistration {
        return collectionReference.document(uid).addSnapshotListener(listener)
    }

    fun listenOnUserChanges(user: User, listener: EventListener<DocumentSnapshot>): ListenerRegistration {
        return listenOnUserChanges(user.uid!!, listener)
    }

    fun registerUser(user:User): Task<Void> {
        return collectionReference.document(user.uid!!).set(user)
    }
}

