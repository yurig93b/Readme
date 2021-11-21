package com.ariel.readme.data.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ariel.readme.data.model.User
import com.ariel.readme.data.repo.UserRepository
import com.google.firebase.auth.FirebaseAuth

class BlankViewModel : ViewModel() {

    val lastUser: MutableLiveData<User?>

    init {
        lastUser = MutableLiveData()

        UserRepository().listenOnUserChanges(FirebaseAuth.getInstance().currentUser!!.uid){ dcs, e ->
                val user = dcs!!.toObject(User::class.java)
                lastUser.value = user
        }

    }
    // TODO: Implement the ViewModel
}

//BlankFragment -> BlankViewModel -> lastUser