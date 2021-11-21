package com.ariel.readme

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.ariel.readme.data.model.User
import com.ariel.readme.data.repo.UserRepository
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val rpo = UserRepository()

        rpo.getUsersByFirstName("Yuri").addOnSuccessListener { docs ->
            for (doc in docs) {
                val us: User = doc.toObject(User::class.java)
            }
        }

        rpo.listenOnUsersChanges { doc, e ->
            val d = doc
            val uuuu = d!!.documentChanges[0].document.toObject(User::class.java)


        }


        val user2 = FirebaseAuth.getInstance().currentUser

        var newuser = User(user2!!.uid,"phone", "fff", "llll")

        rpo.registerUser(newuser)

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