package com.ariel.readme.understand

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ariel.readme.R
import com.ariel.readme.data.model.User
import com.ariel.readme.data.repo.UserRepository
import com.ariel.readme.services.AuthService
import com.google.firebase.auth.FirebaseAuth

class ExampleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example)


        val userRepo = UserRepository()
        val firebase_user = FirebaseAuth.getInstance().currentUser

//      Registering a user
        val myNewUser: User = User(firebase_user!!.uid, "+97258456789", "my first", "my last", true)
        userRepo.registerUser(myNewUser).addOnSuccessListener {
            Toast.makeText(getApplicationContext(), "User registered!", Toast.LENGTH_SHORT)
                .show();
        }

//      Get current User from DB by FirebaseUser
        userRepo.getCurrentUser(AuthService.getCurrentFirebaseUser()!!).addOnSuccessListener { user ->
            Toast.makeText(
                getApplicationContext(),
                "Got user from DB! ${user.obj!!.firstName}",
                Toast.LENGTH_SHORT
            ).show();
        }.addOnFailureListener { e ->
            Toast.makeText(getApplicationContext(), "error ${e.message}", Toast.LENGTH_SHORT)
                .show();
        }

//      Get all managers if any
        userRepo.getAllManagers().addOnSuccessListener { users ->
            Toast.makeText(
                getApplicationContext(),
                "Got managers from DB! len ${users.changes.size}",
                Toast.LENGTH_SHORT
            ).show();
        }.addOnFailureListener { e ->
            Toast.makeText(getApplicationContext(), "error ${e.message}", Toast.LENGTH_SHORT)
                .show();
        }

    }
}