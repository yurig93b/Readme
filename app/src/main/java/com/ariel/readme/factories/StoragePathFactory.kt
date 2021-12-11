package com.ariel.readme.factories

import com.ariel.readme.data.model.User

object StoragePathFactory {
    fun getProfilePicPath(u: User): String {
        return "profiles/${u.uid!!}"
    }
}