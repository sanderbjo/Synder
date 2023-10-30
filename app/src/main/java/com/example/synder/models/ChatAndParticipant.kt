package com.example.synder.models

data class ChatAndParticipant(
    val id: String = "",
    val chat: ChatsFromFirebase = ChatsFromFirebase(),
    val user1: UserProfile = UserProfile(),
    val user2: UserProfile = UserProfile(),
    val latestmessage: Message
)
