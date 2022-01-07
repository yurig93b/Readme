package com.ariel.readme.view.manager

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ariel.readme.data.model.User
import com.ariel.readme.data.repo.*
import com.ariel.readme.factories.RepositoryFactory
import com.ariel.readme.services.AuthService
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.jjoe64.graphview.series.DataPoint
import java.time.Duration
import java.time.Instant
import java.util.*
import kotlin.streams.toList


class ManagerViewModel : ViewModel(){

    private val _user: MutableLiveData<User> = MutableLiveData()
    val user: MutableLiveData<User> = _user

    private val _targetUser: MutableLiveData<User> = MutableLiveData()
    val targetUser: MutableLiveData<User> = _targetUser

    private val _loading: MutableLiveData<Boolean> = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _loadedDataPointsAU: MutableLiveData<Array<DataPoint>> = MutableLiveData()
    val loadedDataPointAU: LiveData<Array<DataPoint>> = _loadedDataPointsAU

    private val _loadedDataPointsMPR: MutableLiveData<Array<DataPoint>> = MutableLiveData()
    val loadedDataPointMPR: LiveData<Array<DataPoint>> = _loadedDataPointsMPR


    @RequiresApi(Build.VERSION_CODES.O)
    fun loadStatistics(){
        val to = Instant.now()
        val from = to.minus(Duration.ofDays(1))

        StatisticsRepository().getStatisticsBetweenTimestamp(
            Timestamp(Date.from(from)), Timestamp(
                Date.from(to))
        ).addOnSuccessListener { data ->

            val mapped = data.changes.stream().map{s -> DataPoint(s.obj.ts?.toDate(), s.obj.active_users.toDouble()) }.toList()
            _loadedDataPointsAU.value = mapped.toTypedArray()
            val mappedMPR = data.changes.stream().map{s -> DataPoint(s.obj.ts?.toDate(), s.obj.mpr.toDouble()) }.toList()
            _loadedDataPointsMPR.value = mappedMPR.toTypedArray()
        }.addOnFailureListener {
            _loadedDataPointsAU.value = arrayOf(DataPoint(0.0,0.0))
            _loadedDataPointsMPR.value = arrayOf(DataPoint(0.0,0.0))
        }
    }

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
            "failed"
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