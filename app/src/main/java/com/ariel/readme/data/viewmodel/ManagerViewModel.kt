package com.ariel.readme.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ariel.readme.data.model.User
import com.ariel.readme.data.repo.ModeledChangedDocuments
import com.ariel.readme.data.repo.ModeledDocument
import com.ariel.readme.data.repo.UserRepository
import com.ariel.readme.factories.RepositoryFactory
import com.ariel.readme.services.AuthService
import com.google.android.gms.tasks.Task
import java.lang.Thread.sleep

class ManagerViewModel : ViewModel(){

    private val _user: MutableLiveData<User> = MutableLiveData()
    val user: MutableLiveData<User> = _user

    private val _targetUser: MutableLiveData<User> = MutableLiveData()
    val targetUser: MutableLiveData<User> = _targetUser

    private val _loading: MutableLiveData<Boolean> = MutableLiveData(true)
    val loading: LiveData<Boolean> = _loading

    fun checkUser(): Task<ModeledDocument<User>> {
        _loading.value = true
        val curUser = AuthService.getCurrentFirebaseUser()
        val task = UserRepository().getCurrentUser(curUser!!)
        task.addOnSuccessListener { doc -> _user.value = doc.obj }
        return task
    }

    fun setTarget(phone: String): Task<ModeledChangedDocuments<User>> {
        val task = UserRepository().getUserByPhone(phone)
        task.addOnSuccessListener { doc ->
            if(doc.changes.isNotEmpty()){
                _targetUser.value = doc.changes[0].obj
            }
        }
        return task
    }

    fun setManager(): Task<Void>? {
        val s = _user.value != null
        val r = _targetUser.value != null
        val g = _user.value!!.manager
        if(_user.value != null && _targetUser.value != null && _user.value!!.manager){
            val updatedUser = _targetUser.value!!.copy(manager = true)
            return RepositoryFactory.getUserRepository().registerUser(updatedUser)
        }
        return null
    }

    fun setBanned(): Task<Void>? {
        if(_user.value != null && _targetUser.value != null && _user.value!!.manager){
            val updatedUser = _targetUser.value!!.copy(banned = true)
            return RepositoryFactory.getUserRepository().registerUser(updatedUser)
        }
        return null
    }

    fun setUnbanned(): Task<Void>? {
        if(_user.value != null && _targetUser.value != null && _user.value!!.manager){
            val updatedUser = _targetUser.value!!.copy(banned = false)
            return RepositoryFactory.getUserRepository().registerUser(updatedUser)
        }
        return null
    }
}