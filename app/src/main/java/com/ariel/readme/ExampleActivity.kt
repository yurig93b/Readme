package com.ariel.readme

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ariel.readme.data.model.User
import com.ariel.readme.data.repo.ModeledDocumentChange
import com.ariel.readme.data.repo.UserRepository
import com.ariel.readme.data.repo.interfaces.IGetChangedModels
import com.ariel.readme.data.repo.interfaces.IGetDocRef
import com.ariel.readme.data.repo.interfaces.IGetModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

class ExampleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example)


        val userRepo = UserRepository()
        val firebase_user = FirebaseAuth.getInstance().currentUser

//      Registering a user
        val myNewUser: User = User(firebase_user!!.uid, "+97258456789", "my first", "my last", true)
        userRepo.registerUser(myNewUser, object : IGetDocRef {
            override fun onSuccess(d: DocumentReference?) {
                Toast.makeText(getApplicationContext(), "User registered!", Toast.LENGTH_SHORT)
                    .show();
            }

            override fun onFailure(e: Exception) {
                Toast.makeText(getApplicationContext(), "ERROR ${e.message} ", Toast.LENGTH_SHORT).show();
            }
        })


//      Get current User from DB by FirebaseUser
        userRepo.getCurrentUser(firebase_user, object : IGetModel<User> {
            override fun onFailure(e: Exception) {
                Toast.makeText(getApplicationContext(), "ERROR ${e.message} ", Toast.LENGTH_SHORT).show();

            }

            override fun onSuccess(d: User?, raw: DocumentSnapshot?, docRef: DocumentReference?) {

                Toast.makeText(getApplicationContext(), "Got user from DB!", Toast.LENGTH_SHORT)
                    .show();
            }
        })

//      Get all managers if any
        userRepo.getAllManagers(object: IGetChangedModels<User>{
            override fun onSuccess(d: List<ModeledDocumentChange<User>>, raw: QuerySnapshot?) {
                Toast.makeText(getApplicationContext(), "Got ${d.size} manager!", Toast.LENGTH_SHORT)
                    .show();
            }

            override fun onFailure(e: Exception) {
                Toast.makeText(getApplicationContext(), "ERROR ${e.message} ", Toast.LENGTH_SHORT).show();
            }
        })
    }
}