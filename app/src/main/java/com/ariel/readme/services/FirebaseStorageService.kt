package com.ariel.readme.services

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.io.InputStream

class FirebaseStorageService: IStorageService {
    private val storageRef = FirebaseStorage.getInstance().reference

    fun putBytes(path: String,  data: ByteArray): UploadTask {
        return storageRef.child(path).putBytes(data)
    }

    fun putStream(path: String,  data: InputStream): UploadTask {
        return storageRef.child(path).putStream(data)
    }

    fun putFile(path: String, file: Uri): UploadTask {
        return storageRef.child(path).putFile(file)
    }

    fun getDownloadUrl(path: String): Task<Uri> {
        return storageRef.child(path).downloadUrl
    }
}