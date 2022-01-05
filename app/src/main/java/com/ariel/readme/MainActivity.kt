package com.ariel.readme

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.ariel.readme.data.model.User
import com.ariel.readme.factories.RepositoryFactory
import com.ariel.readme.profile.UserProfileActivity
import com.ariel.readme.services.AuthService
import com.ariel.readme.services.MessageHandlingService
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import java.util.*


class MainActivity : AppCompatActivity() {
    companion object {
        val CHANNEL_ID_HOT_WORDS = "hotwords"
    }


    private fun initNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = "Hot words alerts"
            val descriptionText = "Channel for alerting on hot word messages."
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(CHANNEL_ID_HOT_WORDS, name, importance)
            mChannel.description = descriptionText
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }

    private fun goToExampleActivity() {
        initNotificationChannels()
        val userFb = AuthService.getCurrentFirebaseUser()

        RepositoryFactory.getUserRepository().getUserById(userFb!!.uid)
            .addOnSuccessListener { userObj ->
                if (userObj.obj === null) {
                    val myNewUser = User(userFb.uid, userFb.phoneNumber, "", "", false)
                    RepositoryFactory.getUserRepository().registerUser(myNewUser)
                        .addOnSuccessListener {
                            Toast.makeText(
                                getApplicationContext(),
                                "User registered!",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            val intent = Intent(this, UserProfileActivity::class.java)
                            intent.putExtra(UserProfileActivity.ARG_NEW_USER, true)
                            intent.setFlags(intent.flags or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)
                        }.addOnFailureListener { e ->
                            Toast.makeText(
                                getApplicationContext(),
                                "Something went wrong with user registration.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                } else {
                    if (userObj.obj.banned) {
                        Toast.makeText(
                            getApplicationContext(),
                            "You are banned from the system.",
                            Toast.LENGTH_SHORT
                        ).show()

                        FirebaseAuth.getInstance().signOut()
                        this.finish();
                    }

                    MessageHandlingService().ensureUserTokenIsSet()
                    val intent = Intent(this, SelectContact::class.java)
                    intent.setFlags(intent.flags or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
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
        } else {
            goToExampleActivity()
        }
// ...
    }
}