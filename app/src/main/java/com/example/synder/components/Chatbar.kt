package com.example.synder.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Chatbar() {
    var textValue by remember { mutableStateOf("") }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { /* Define action here for the first IconButton */ },
            modifier = Modifier.padding(8.dp)
        ) {
            Icon(Icons.Default.Build, contentDescription = null)
        }
        TextField(
            value = textValue,
            onValueChange = { newTextValue -> textValue = newTextValue },
            placeholder = { Text("Skriv her") },
            modifier = Modifier.weight(1f) // Take up maximum available space
        )
        IconButton(
            onClick = { /* Define action here for the second IconButton */ },
            modifier = Modifier.padding(8.dp)
        ) {
            Icon(Icons.Default.Send, contentDescription = null)
        }
    }
}
