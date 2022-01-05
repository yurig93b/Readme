package com.ariel.readme.services

import android.content.ContentValues.TAG
import android.util.Log
import com.ariel.readme.data.repo.UserRepository
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService


class MessageHandlingService : FirebaseMessagingService() {

    fun ensureUserTokenIsSet(): Task<Boolean> {
        return FirebaseMessaging.getInstance().token.continueWith(object :
            Continuation<String, Boolean> {
            override fun then(tres: Task<String>): Boolean {
                if (tres.exception != null) {
                    throw tres.exception!!
                }
                onNewToken(tres.getResult()!!)
                return true
            }
        })
    }


    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.d(TAG, "Refreshed token: $token")
        val user = AuthService.getCurrentFirebaseUser()
        val userRepo = UserRepository()

        if (user == null) {
            return
        }
        userRepo.getCurrentUser(AuthService.getCurrentFirebaseUser()!!)
            .addOnSuccessListener { modeledUser ->
                val updatedUser = modeledUser.obj!!.copy(token=token)
                userRepo.registerUser(updatedUser).addOnFailureListener { e ->
                    Log.d(TAG, "Failed updating user token. ${e.message}")
                }
            }

    }
}