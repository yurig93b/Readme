package com.ariel.readme.data.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ariel.readme.data.model.User
import com.ariel.readme.factories.RepositoryFactory
import com.ariel.readme.services.AuthService
import com.google.android.gms.tasks.Task

class ManagerViewModel : ViewModel(){

    private val _user: MutableLiveData<User> = MutableLiveData()
    val user: MutableLiveData<User> = _user

    private val _targetUser: MutableLiveData<User> = MutableLiveData()
    val targetUser: MutableLiveData<User> = _targetUser

    private fun setUsers(phone: String): Boolean{
        val curUser = AuthService.getCurrentFirebaseUser()
        RepositoryFactory.getUserRepository().getCurrentUser(curUser!!).addOnSuccessListener { doc ->
            _user.value = doc.obj }
        RepositoryFactory.getUserRepository().getUserByPhone(phone).addOnSuccessListener { doc ->
            if(doc.changes.isNotEmpty()){
                _targetUser.value = doc.changes[0].obj
            }
        }
        return (_user.value != null && _targetUser.value != null)
    }

    fun setManager(phone: String): Task<Void>? {
        if(setUsers(phone) && user.value!!.manager){
            val updatedUser = targetUser.value!!.copy(manager = true)
            return RepositoryFactory.getUserRepository().registerUser(updatedUser)
        }
        return null
    }

    fun setBanned(phone: String): Task<Void>? {
        if(setUsers(phone) && user.value!!.manager){
            val updatedUser = targetUser.value!!.copy(banned = true)
            return RepositoryFactory.getUserRepository().registerUser(updatedUser)
        }
        return null
    }

    fun setUnbanned(phone: String): Task<Void>? {
        if(setUsers(phone) && user.value!!.manager){
            val updatedUser = targetUser.value!!.copy(banned = false)
            return RepositoryFactory.getUserRepository().registerUser(updatedUser)
        }
        return null
    }
}