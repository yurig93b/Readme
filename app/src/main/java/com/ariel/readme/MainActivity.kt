package com.ariel.readme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.firebase.ui.auth.AuthUI

import android.content.Intent
import androidx.activity.result.ActivityResultCallback
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract

import androidx.activity.result.ActivityResultLauncher
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import java.util.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val txtview = findViewById(R.id.myKoteret) as TextView;
        txtview.text ="Yuri";


        val user = FirebaseAuth.getInstance().currentUser

        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder().setAvailableProviders(
                arrayListOf(AuthUI.IdpConfig.PhoneBuilder().build())).build()

        val signInLauncher = registerForActivityResult(
            FirebaseAuthUIActivityResultContract()
        ) { result: FirebaseAuthUIAuthenticationResult? ->

            val user2 = FirebaseAuth.getInstance().currentUser
        }

// ...
        signInLauncher.launch(signInIntent)
    }
}