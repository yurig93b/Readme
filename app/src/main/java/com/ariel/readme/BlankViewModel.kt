package com.ariel.readme

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ariel.readme.data.model.User
import com.ariel.readme.data.repo.UserRepository
import com.google.firebase.firestore.DocumentChange

class BlankViewModel : ViewModel() {

    val lastUser: MutableLiveData<User?>

    init {
        lastUser = MutableLiveData()
        UserRepository().listenOnUserChanges{dcs, e ->
            for(dc in dcs!!.documentChanges){
                val user = dc.document.toObject(User::class.java)
                lastUser.value = user
            }
        }

    }
    // TODO: Implement the ViewModel
}