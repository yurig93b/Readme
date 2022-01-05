package com.ariel.readme.data.repo

import android.os.Build
import androidx.annotation.RequiresApi
import com.ariel.readme.data.model.Chat
import com.ariel.readme.data.model.HotWord
import com.ariel.readme.data.model.User
import com.ariel.readme.data.repo.interfaces.IChatRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import java.util.stream.Collectors
import com.ariel.readme.data.repo.interfaces.IGetChangedModels


class ChatRepository : FirebaseRepository<Chat>(), IChatRepository {
    override val rootNode: String
        get() = "chats"

    @RequiresApi(Build.VERSION_CODES.N)
    override fun createChat(chat:Chat): Task<DocumentReference> {
        chat.participantsStr =  chat.participants.sorted().stream().collect(Collectors.joining(","))
        return collRef.add(chat)
    }

    override fun getChat(cid: String): Task<ModeledDocument<Chat>> {
        return HookGetDocumentSnapshot(collRef.document(cid).get())
    }

    override fun getChatsByUser(user:User): Task<ModeledChangedDocuments<Chat>> {
        return HookQuery(collRef.whereArrayContains(Chat::participants.name, user.uid!!).get())
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun getChatsByUsers(participants:List<String>): Task<ModeledChangedDocuments<Chat>> {
        val participantsStr = participants.sorted().stream().collect(Collectors.joining(","))
        return HookQuery(collRef.whereEqualTo(Chat::participantsStr.name, participantsStr).get())
    }
//    override fun listenOnChats(user: User, listener: EventListener<QuerySnapshot>): ListenerRegistration {
//        return collRef.whereArrayContains(Chat::participants.name, user.uid!!).addSnapshotListener(listener)
//    }
    override fun listenOnChats(uid: String, listener: IGetChangedModels<Chat>): ListenerRegistration {
        return HookListenQuery(collRef.whereArrayContains(Chat::participants.name, uid),listener)
    }

    fun removeChat( cid: String): Task<Void> {
        return collRef.document(cid).delete()
    }
}


