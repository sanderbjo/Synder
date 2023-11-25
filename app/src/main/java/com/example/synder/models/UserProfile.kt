package com.example.synder.models

import com.google.firebase.firestore.DocumentId


data class UserProfile(
    @DocumentId val id: String = "",
    val name: String = "",
    val age: String = "",
    val bio: String = "",
    val kjonn: String = "",
    val serEtter: String = "",
    val likedUsers: List<String> = emptyList(),
    val dislikedUsers: List<String> = emptyList(),
    val matches: List<String> = emptyList(),
    val chats: List<String> = emptyList(),
    val coordinates: Coordinates = Coordinates(latitude = 0.0, longitude = 0.0),
)
