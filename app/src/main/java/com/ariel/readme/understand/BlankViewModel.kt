package com.ariel.readme.understand

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ariel.readme.data.model.User
import com.ariel.readme.data.repo.UserRepository
import com.ariel.readme.data.repo.interfaces.IGetChangedModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot

class BlankViewModel : ViewModel() {

    val lastUser: MutableLiveData<User?>

    init {
        lastUser = MutableLiveData()

        UserRepository().listenOnUserChanges(FirebaseAuth.getInstance().currentUser!!.uid, object : IGetChangedModel<User>{
            override fun onSuccess(d: User?, raw: DocumentSnapshot?) {
                lastUser.value = d
            }

            override fun onFailure(d: Exception) {

            }
        })

    }
}

//BlankFragment -> BlankViewModel -> lastUser