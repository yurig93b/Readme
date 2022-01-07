package com.ariel.readme.services

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.UploadTask
import java.io.InputStream

interface IStorageService {
    fun putBytes(path: String,  data: ByteArray): UploadTask
    fun putStream(path: String,  data: InputStream): UploadTask
    fun putFile(path: String, file: Uri): UploadTask
    fun getDownloadUrl(path: String): Task<Uri>
}