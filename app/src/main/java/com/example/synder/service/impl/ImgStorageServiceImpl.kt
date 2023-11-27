package com.example.synder.service.impl

import android.net.Uri
import com.example.synder.service.ImgStorageService
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import javax.inject.Inject

class ImgStorageServiceImpl @Inject constructor(private val storage: FirebaseStorage): ImgStorageService
{
    override var storageRef = storage.reference

    override suspend fun addFile(uri: Uri?, userid: String ) {
        val imageRef = storageRef.child("images/${userid}.jpg")

        if (uri != null) {
            imageRef.putFile(uri)
        }
    }

    override suspend fun getImgRef(userid: String): StorageReference {
        val imageRef = storageRef.child("images/${userid}.jpg")

        return imageRef
    }

    override suspend fun getStorageRef(): StorageReference {
        return storageRef
    }
}