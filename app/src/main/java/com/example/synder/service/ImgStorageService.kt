package com.example.synder.service

import android.net.Uri
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import java.util.Objects

interface ImgStorageService {
    var storageRef: StorageReference

    //var imagesRef: StorageReference
    suspend fun addFile(uri: Uri?,userid:String)
    suspend fun getImgRef(userid: String): StorageReference?
    suspend fun getStorageRef(): StorageReference
}