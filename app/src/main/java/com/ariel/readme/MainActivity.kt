package com.ariel.readme

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ariel.readme.data.model.Chat
import com.ariel.readme.data.model.Message
import com.ariel.readme.data.model.User
import com.ariel.readme.data.repo.ChatRepository
import com.ariel.readme.data.repo.MessageRepository
import com.ariel.readme.data.repo.UserRepository
import com.google.firebase.Timestamp.now
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val user_repo = UserRepository()
        val chat_repo = ChatRepository()
        val msg_repo = MessageRepository()

        val myNewUser: User = User(null, "+97258456789", "my first", "my last")
        val firebase_user = FirebaseAuth.getInstance().currentUser

//        user_repo.getCurrentUser(firebase_user!!).addOnSuccessListener{doc ->
//            val converted_user: User? = doc.toObject(User::class.java)
//
//            user_repo.listenOnUserChanges(converted_user!!, { value, e ->
//                value.toObject()
//            })
//        }

//        user_repo.listenOnUsersChanges({docs, e ->{
//            docs.
//        }})
//

//        user_repo.getCurrentUser(firebase_user!!).addOnSuccessListener{doc ->
//            val converted_user: User? = doc.toObject(User::class.java)
//        }
//
//        user_repo.getAllManagers().addOnSuccessListener{ docs ->
//            for(doc in docs.documents){
//                val manager :User? = doc.toObject(User::class.java)
//                val aaa = null
//        }}
//        val user2 = FirebaseAuth.getInstance().currentUser
//
//        // Get current user by FirebaseUser object
//        user_repo.getCurrentUser(user2!!).addOnSuccessListener{ docs ->
//            // Cast Map to object
//            val u = docs.toObject(User::class.java)
//            // Create a Chat
//            chat_repo.createChat(Chat(null, listOf(u!!.uid!!))).addOnSuccessListener{ dref ->
//
//                // Get Chat by ID returned by firebase
//                chat_repo.getChat(dref.id).addOnSuccessListener{ chatDoc ->
//                    // Cast to Chat Object
//                    val created_chat = chatDoc.toObject(Chat::class.java)
//
//                    val put_msg = Message(null, created_chat!!.cid!!, u.uid!!, now(), text = "This is my msg")
//                    msg_repo.createMessage(put_msg).addOnSuccessListener { mref ->
//
//                    }
//                }
//            }
//        }


//// Add a new document with a generated ID
//        db.collection("users")
//            .add(user)
//            .addOnSuccessListener { documentReference ->
//                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
//            }
//            .addOnFailureListener { e ->
//                Log.w(TAG, "Error adding document", e)
//            }


//        if(user == null){
//            val signInIntent = AuthUI.getInstance()
//                .createSignInIntentBuilder().setAvailableProviders(
//                    arrayListOf(AuthUI.IdpConfig.PhoneBuilder().build())).build()
//
//            val signInLauncher = registerForActivityResult(
//                FirebaseAuthUIActivityResultContract()
//            ) { result: FirebaseAuthUIAuthenticationResult? ->
//
//        val user2 = FirebaseAuth.getInstance().currentUser
//
//
//            }
//        }


// ...
//        signInLauncher.launch(signInIntent)
    }
}