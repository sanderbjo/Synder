package com.example.synder.service

import android.net.Uri
import com.google.firebase.storage.StorageReference

interface ImgStorageService {
    var storageRef: StorageReference

    suspend fun addFile(uri: Uri?,userid:String)
    suspend fun getImgRef(userid: String): StorageReference?
    suspend fun getStorageRef(): StorageReference
}