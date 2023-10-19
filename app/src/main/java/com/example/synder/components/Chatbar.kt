package com.example.synder.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text

@Composable
fun Chatbar ( ) {
    Row {
        Icon(imageVector = Icons.Default.Build, contentDescription = null)
        Text(text = "Skriv her")
        Icon(imageVector = Icons.Default.Search, contentDescription = null)
    }
}