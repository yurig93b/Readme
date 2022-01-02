package com.ariel.readme

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ariel.readme.data.model.User
import com.ariel.readme.data.repo.ChatRepository
import com.ariel.readme.data.repo.UserRepository
import com.ariel.readme.services.AuthService
import com.ariel.readme.services.MessageHandlingService
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private fun goToExampleActivity(){
        AuthService.getCurrentFirebaseUser()?.let {
            UserRepository().registerUser(
                User(uid=it.uid, it.phoneNumber, firstName = "rawan",
                lastName = "shareef" )
            ).addOnSuccessListener{
                MessageHandlingService().ensureUserTokenIsSet()
            }
        }

       // MessageHandlingService().ensureUserTokenIsSet(
        val intent = Intent(this, SelectContact::class.java)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val user = FirebaseAuth.getInstance().currentUser//אופיקט מפיירביס שיש  בתוכו UID ומספר טלפון

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
        val chat_repo= ChatRepository()
        val user_repo= UserRepository()

        //איך שולפים מידע מפיירביס

        // chat_repo.getChatsByUser(user_repo.getCurrentUser(user!!))
//        val newChat:Chat=Chat("hjfudf")
//        chat_repo.createChat(newChat).addOnCanceledListener {
//
//        }
// ...
    }
}