package com.ariel.readme.view.profile

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ariel.readme.data.model.User
import com.ariel.readme.factories.RepositoryFactory
import com.ariel.readme.factories.StorageFactory
import com.ariel.readme.factories.StoragePathFactory
import com.ariel.readme.services.AuthService
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream

class UserProfileViewModel : ViewModel() {
    val errors: MutableLiveData<MutableSet<Int>> = MutableLiveData(hashSetOf())

    private val _loading: MutableLiveData<Boolean> = MutableLiveData(true)
    val loading: LiveData<Boolean> = _loading

    private val _user: MutableLiveData<User> = MutableLiveData()
    val user: MutableLiveData<User> = _user

    fun updatePublicPic(bitmap: Bitmap): StorageTask<UploadTask.TaskSnapshot> {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
        val image = stream.toByteArray()

        val path = StoragePathFactory.getProfilePicPath(user.value!!)

        _loading.value = true
        return StorageFactory.getStorage().putBytes(path, image).addOnCompleteListener {
            _loading.value = false
        }

    }

    fun saveProfile(firstName: String?, lastName: String?): Task<Void>? {
        if (user.value != null) {
            _loading.value = true
            val updatedUser = user.value!!.copy(firstName = firstName, lastName = lastName)
            return RepositoryFactory.getUserRepository().registerUser(updatedUser)
                .addOnCompleteListener {
                    _loading.value = false
                }
        }
        return null
    }

    fun loadUser() {
        _loading.value = true
        val fbUser = AuthService.getCurrentFirebaseUser()
        RepositoryFactory.getUserRepository().getCurrentUser(fbUser!!).addOnSuccessListener { doc ->
            _user.value = doc.obj
        }.addOnCompleteListener {
            _loading.value = false
        }
    }

    init {
    }
}