package com.ariel.readme

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

import android.util.Log
import com.ariel.readme.data.domain.User

import com.ariel.readme.data.repo.FirebaseRepository
import com.ariel.readme.data.repo.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.Exception


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val txtview :TextView = findViewById(R.id.myKoteret);
        txtview.text ="Yuri";

        val rpo = UserRepository()

        class mmm  :FirebaseRepository.FirebaseDatabaseRepositoryCallback<User>{
            override fun onSuccess(result: List<User>?) {
                Log.d(TAG, "heyy")

            }

            override fun onError(e: Exception?) {
                Log.d(TAG, "bye")

                TODO("Not yet implemented")
            }


        }

        val list:FirebaseRepository.FirebaseDatabaseRepositoryCallback<User> = mmm()
        rpo.addListener(list)

        val userauth = FirebaseAuth.getInstance().currentUser
        val db = Firebase.firestore
        // Create a new user with a first and last name
        val user = hashMapOf(
            "first" to "Ada",
            "last" to "Lovelace",
            "born" to 1815
        )

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
//                val user2 = FirebaseAuth.getInstance().currentUser
//
//
//            }
//        }



// ...
//        signInLauncher.launch(signInIntent)
    }
}