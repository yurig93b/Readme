package com.ariel.readme.factories

import com.ariel.readme.data.model.User
import com.ariel.readme.services.FirebaseStorageService

object StorageFactory {
    fun getStorage(): FirebaseStorageService {
        return FirebaseStorageService()
    }
}