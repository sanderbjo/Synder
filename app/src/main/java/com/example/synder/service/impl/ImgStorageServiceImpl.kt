package com.example.synder.service.impl

import android.net.Uri
import com.example.synder.service.ImgStorageService
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ImgStorageServiceImpl @Inject constructor(private val storage: FirebaseStorage): ImgStorageService
{
    override var storageRef = storage.reference

    override suspend fun addFile(uri: Uri?, userid: String ) {
        val imageRef = storageRef.child("images/${userid}.jpg")

        if (uri != null) {
            imageRef.putFile(uri)
                //.addOnSuccessListener {taskSnapshot ->
                //    taskSnapshot.metadata?.reference?.downloadUrl
                //}
                //.addOnFailureListener {exception ->
                //}
        }
    }

    override suspend fun getImgRef(userid: String): StorageReference {
        val imageRef = storageRef.child("images/${userid}.jpg")
        //var img: Uri = Uri.EMPTY
        //val imageDL = imageRef.downloadUrl.addOnSuccessListener {
        //}
        //firestore.collection(StorageServiceImpl.USERS).document(userId).get().await().toObject()

        //imageRef.downloadUrl.addOnCompleteListener{
        //    if (it.isSuccessful) {
        //        img = it.result
        //    }
        //}
        return imageRef
        //return img
        //val image = imageRef.getBytes(1024*1024)
    }

    override suspend fun getStorageRef(): StorageReference {
        return storageRef
    }
}