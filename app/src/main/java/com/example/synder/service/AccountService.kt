package com.example.synder.service

import com.example.synder.models.UserProfile
import com.google.firebase.firestore.auth.User
import kotlinx.coroutines.flow.Flow


interface AccountService {
val currentUserId: String
val  hasUser: Boolean
val currentUser: Flow<UserProfile>

suspend fun authenticate(email: String, password: String, onResult: (Throwable?) -> Unit)
suspend fun linkAccount(email: String, password: String, onResult: (Throwable?) -> Unit)
suspend fun signOut()

}