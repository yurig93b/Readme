package com.ariel.readme.data.mappers

import com.ariel.readme.data.domain.User
import com.ariel.readme.data.entities.UserEntity

class UserMapper: FirebaseMapper<UserEntity, User>() {
    override fun map(from: UserEntity): User {
        TODO("Not yet implemented")
    }
}