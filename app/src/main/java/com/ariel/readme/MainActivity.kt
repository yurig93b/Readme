package com.ariel.readme

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ariel.readme.message.EmptyActivity
import com.ariel.readme.profile.UserProfileActivity
import com.ariel.readme.services.AuthService
import com.ariel.readme.services.MessageHandlingService
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private fun goToExampleActivity(){

        MessageHandlingService().ensureUserTokenIsSet()

        val intent = Intent(this, SettingActivity::class.java)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val user = FirebaseAuth.getInstance().currentUser

        if (user == null) {
            val signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder().setAvailableProviders(
                    arrayListOf(AuthUI.IdpConfig.PhoneBuilder().build())
                ).build()

            val signInLauncher = registerForActivityResult(
                FirebaseAuthUIActivityResultContract()
            ) { result: FirebaseAuthUIAuthenticationResult? ->
                goToExampleActivity()
            }
            signInLauncher.launch(signInIntent)
        }

        else{
            goToExampleActivity()
        }
// ...
    }
}