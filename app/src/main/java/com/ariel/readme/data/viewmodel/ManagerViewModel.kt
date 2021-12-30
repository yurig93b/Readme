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


class ManagerViewModel : ViewModel(){

    private val _user: MutableLiveData<User> = MutableLiveData()
    val user: MutableLiveData<User> = _user

    private val _targetUser: MutableLiveData<User> = MutableLiveData()
    val targetUser: MutableLiveData<User> = _targetUser

    private val _loading: MutableLiveData<Boolean> = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    fun checkUser(): Task<ModeledDocument<User>> {
        _loading.value = true
        val curUser = AuthService.getCurrentFirebaseUser()
        val task = UserRepository().getCurrentUser(curUser!!)
        task.addOnSuccessListener { doc -> _user.value = doc.obj }
        return task
    }

    private fun checkText(text : String): Boolean {    //check for bad input
        if(text == _user.value!!.phone){ return false}
        if(text.length <= 24 && text != "") {
            val badChars: List<Char> = listOf(
                ' ', ';', '$', '|', '&',
                '(', ')', '[', ']', '{',
                '}', '<', '>', '\\', '/',
                '\n', '\t', '\r',
                '.','!','=','?'
            )
            for (char in badChars) {
                if (char in text) {
                    return false
                }
            }
            return true
        }
        return false
    }

    fun setTarget(phone: String): Task<ModeledChangedDocuments<User>> {
        val number : String = if(checkText(phone)){
            phone
        } else{
            "-1"
        }
        val task = UserRepository().getUserByPhone(number)
        task.addOnSuccessListener { doc ->
            if(doc.changes.isNotEmpty()){
                _targetUser.value = doc.changes[0].obj
            }
        }
        return task
    }

    fun setManager(): Task<Void>? {
        if(_user.value != null && _targetUser.value != null && _user.value!!.manager){
            val updatedUser = _targetUser.value!!.copy(manager = true)
            return RepositoryFactory.getUserRepository().registerUser(updatedUser).addOnCompleteListener {
                _loading.value = false
            }
        }
        _loading.value = false
        return null
    }

    fun setBanned(): Task<Void>? {
        if(_user.value != null && _targetUser.value != null && _user.value!!.manager){
            val updatedUser = _targetUser.value!!.copy(banned = true)
            return RepositoryFactory.getUserRepository().registerUser(updatedUser).addOnCompleteListener {
                _loading.value = false
            }
        }
        _loading.value = false
        return null
    }

    fun setUnbanned(): Task<Void>? {
        if(_user.value != null && _targetUser.value != null && _user.value!!.manager){
            val updatedUser = _targetUser.value!!.copy(banned = false)
            return RepositoryFactory.getUserRepository().registerUser(updatedUser).addOnCompleteListener {
                _loading.value = false
            }
        }
        _loading.value = false
        return null
    }
}