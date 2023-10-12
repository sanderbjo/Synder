package com.example.synder.service.impl

import com.example.synder.models.UserProfile
import com.example.synder.service.StorageService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.dataObjects
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StorageServiceImpl
@Inject
constructor(private val firestore: FirebaseFirestore) : StorageService {
    override val users: Flow<List<UserProfile>>
        get() = firestore.collection("users").dataObjects()

    override suspend fun getUser(userId: String): UserProfile? =
        firestore.collection("users").document(userId).get().await().toObject()


    override suspend fun saveUser(user: UserProfile): String =
        firestore.collection("users").add(user).await().id

}