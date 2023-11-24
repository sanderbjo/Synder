package com.example.synder.service.impl

import android.net.Uri
import com.example.synder.models.UserProfile
import com.example.synder.service.AccountService
import com.example.synder.service.ImgStorageService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AccountServiceImpl @Inject constructor(private val auth: FirebaseAuth) : AccountService {
    override val currentUserId: String
        get() = auth.currentUser?.uid.orEmpty()
    override val hasUser: Boolean
        get() = auth.currentUser != null
    override val currentUser: Flow<UserProfile>
        get() = callbackFlow {
            val listener = FirebaseAuth.AuthStateListener { auth ->
                this.trySend(auth.currentUser?.let { UserProfile(it.uid) } ?: UserProfile())
            }
            auth.addAuthStateListener(listener)
            awaitClose { auth.removeAuthStateListener(listener) }
        }

    override suspend fun authenticate(
        email: String,
        password: String,
        onResult: (Throwable?) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { onResult(it.exception) }.await()
    }

    override suspend fun linkAccount(
        email: String,
        password: String,
        name: String,
        age: Float,
        bio: String,
        profileImageUri: Uri?,
        kjonn: String,
        serEtter: String,
        onResult: (Throwable?) -> Unit
    ) {

        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener { onResult(it.exception) }.await()
        //UserProfile user = new UserProfile(currentUserId, name, age.toString(),bio, profileImageUrl, kjonn, serEtter)
        //val profileimg =

        FirebaseFirestore.getInstance().collection("users").document(currentUserId).set(UserProfile(currentUserId, name, age.toInt().toString(),bio ,kjonn, serEtter))
        //auth.createUserWithEmailAndPassword(email, password).
        //bilde til storage
    }

    override suspend fun signOut() {
    auth.signOut()
    }
}