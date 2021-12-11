package com.ariel.readme.services

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

object AuthService{

    fun isLoggedIn(): Boolean {
        return getCurrentFirebaseUser() != null
    }

    fun getCurrentFirebaseUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }
}

