package com.ariel.readme.services

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

object AuthService{
    private var _firebase_user: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    fun isLoggedIn(): Boolean {
        return _firebase_user != null
    }

    fun getCurrentFirebaseUser(): FirebaseUser? {
        _firebase_user = _firebase_user ?: FirebaseAuth.getInstance().currentUser
        return _firebase_user
    }
}

