package com.ariel.readme.factories

import com.ariel.readme.data.repo.ChatRepository
import com.ariel.readme.data.repo.HotWordRepository
import com.ariel.readme.data.repo.MessageRepository
import com.ariel.readme.data.repo.UserRepository
import com.ariel.readme.data.repo.interfaces.IChatRepository
import com.ariel.readme.data.repo.interfaces.IHotWordRepository
import com.ariel.readme.data.repo.interfaces.IMessageRepository
import com.ariel.readme.data.repo.interfaces.IUserRepository
import com.firebase.ui.auth.data.model.User
import com.google.firebase.database.core.Repo

object RepositoryFactory {
        // TODO: add caching
        fun getChatRepository(): IChatRepository {
            return ChatRepository()
        }
        fun getMessageRepository(): IMessageRepository {
            return MessageRepository()
        }

        fun getUserRepository(): IUserRepository {
            return UserRepository()
        }

        fun getHotWordRepository(): IHotWordRepository{
            return HotWordRepository()
        }
}