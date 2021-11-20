package com.ariel.readme.data.repo

import com.ariel.readme.data.domain.User
import com.ariel.readme.data.entities.UserEntity
import com.ariel.readme.data.mappers.UserMapper

class UserRepository: FirebaseRepository<UserEntity, User>(UserMapper()) {
    override val rootNode: String?
        get() = "users"

}