package com.ariel.readme

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.ariel.readme.data.repo.StatisticsRepository
import com.ariel.readme.message.EmptyActivity
import com.ariel.readme.profile.UserProfileActivity
import com.ariel.readme.services.AuthService
import com.ariel.readme.services.MessageHandlingService
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.jjoe64.graphview.series.DataPoint
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.util.*
import java.util.stream.Collectors

class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    private fun goToExampleActivity(){
        val intent = Intent(this, SettingActivity::class.java)
        startActivity(intent)
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
        }

        else{
            goToExampleActivity()
        }
// ...
    }
}