package com.ariel.readme.data.repo

import com.ariel.readme.data.model.Chat
import com.ariel.readme.data.model.User
import com.ariel.readme.data.repo.interfaces.IChatRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*

class ChatRepository : FirebaseRepository<Chat>(), IChatRepository {
    override val rootNode: String
        get() = "chats"

    override fun createChat(chat:Chat): Task<DocumentReference> {
        return collRef.add(chat)
    }

    override fun getChat(cid: String): Task<ModeledDocument<Chat>> {
        return HookGetDocumentSnapshot(collRef.document(cid).get())
    }

    override fun getChatsByUser(user:User): Task<ModeledChangedDocuments<Chat>> {
        return HookQuery(collRef.whereArrayContains(Chat::participants.name, user.uid!!).get())
    }

    override fun listenOnChats(user: User, listener: EventListener<QuerySnapshot>): ListenerRegistration {
        return collRef.whereArrayContains(Chat::participants.name, user.uid!!).addSnapshotListener(listener)
    }

}

