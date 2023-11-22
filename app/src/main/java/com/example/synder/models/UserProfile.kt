package com.example.synder.models

import android.net.Uri
import com.google.firebase.firestore.DocumentId


data class UserProfile(
    @DocumentId val id: String = "",
    val name: String = "",
    val age: String = "",
    val bio: String = "",
    val kjonn: String = "",
    val serEtter: String = ""
)
