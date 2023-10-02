package com.example.synder.models

import androidx.compose.ui.graphics.vector.ImageVector

data class Chat  (
    val name: String,
    val latestChat: String,
    val latestRecieved: String,
    val icon: ImageVector
)