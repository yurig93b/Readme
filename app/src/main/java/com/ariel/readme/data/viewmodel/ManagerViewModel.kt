package com.ariel.readme.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ariel.readme.data.model.Message
import com.ariel.readme.data.model.User
import com.ariel.readme.data.repo.MessageRepository
import com.ariel.readme.data.repo.ModeledChangedDocuments
import com.ariel.readme.data.repo.ModeledDocument
import com.ariel.readme.data.repo.UserRepository
import com.ariel.readme.factories.RepositoryFactory
import com.ariel.readme.services.AuthService
import com.google.android.gms.tasks.Task
import java.text.SimpleDateFormat


class ManagerViewModel : ViewModel(){

    private val _user: MutableLiveData<User> = MutableLiveData()
    val user: MutableLiveData<User> = _user

    private val _targetUser: MutableLiveData<User> = MutableLiveData()
    val targetUser: MutableLiveData<User> = _targetUser

    private val _loading: MutableLiveData<Boolean> = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val utc = 2

    private val _dataPoints: MutableLiveData<Array<Array<Double>>> = MutableLiveData(arrayOf(arrayOf(0.0,0.0),
        arrayOf(1.0,0.0),arrayOf(2.0,0.0),arrayOf(3.0,0.0),arrayOf(4.0,0.0),arrayOf(5.0,0.0),arrayOf(6.0,0.0),
        arrayOf(7.0,0.0),arrayOf(8.0,0.0),arrayOf(9.0,0.0),arrayOf(10.0,0.0),arrayOf(11.0,0.0),arrayOf(12.0,0.0),
        arrayOf(13.0,0.0),arrayOf(14.0,0.0),arrayOf(15.0,0.0),arrayOf(16.0,0.0),arrayOf(17.0,0.0),arrayOf(18.0,0.0),
        arrayOf(19.0,0.0),arrayOf(20.0,0.0),arrayOf(21.0,0.0),arrayOf(22.0,0.0),arrayOf(23.0,0.0)))
    val dataPoints: MutableLiveData<Array<Array<Double>>> = _dataPoints

    fun checkUser(): Task<ModeledDocument<User>> {
        _loading.value = true
        val curUser = AuthService.getCurrentFirebaseUser()
        val task = UserRepository().getCurrentUser(curUser!!)
        task.addOnSuccessListener { doc -> _user.value = doc.obj }
        return task
    }

    private fun checkText(text : String): Boolean {    //check for bad input
        if(text == _user.value!!.phone){ return false}
        if(text.length <= 12 && text != "") {
            val badChars: List<Char> = mutableListOf(
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

    fun setGraph(time: com.google.firebase.Timestamp): Task<ModeledChangedDocuments<Message>> {
        return MessageRepository().getChatMessagesByTime(time).addOnSuccessListener { doc ->
            _loading.value = true
            _dataPoints.value!!.forEach {
                point -> point[1] = 0.0
            }
            doc.changes.forEach { msg ->
                val hour = SimpleDateFormat("HH").format(msg.obj.ts!!.toDate()).toInt() + utc
                for (i in 0..23){
                    if(hour == i){
                        _dataPoints.value!![i][1] = _dataPoints.value!![i][1]+1
                    }
                }
            }
        }.addOnCompleteListener { _loading.value = false }
    }
}