package com.ariel.readme.factories

import com.ariel.readme.data.model.User
import com.ariel.readme.services.AuthService
import com.ariel.readme.services.FirebaseStorageService
import com.ariel.readme.services.IStorageService
import com.google.firebase.ktx.Firebase

object ServiceFactory {
    fun getAuthService(): AuthService {
        return AuthService
    }

    fun getStorageService(): IStorageService {
        return FirebaseStorageService()
    }

}