package com.example.synder.screen.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.SynderTheme


@Composable
fun SettingsScreen(){
    
    LazyColumn(){
        item{
            SettingItem(
                label = "App Theme",
                value = "Light",
                onClick = {}
            )
        }
        item{
            SettingItem(
                label = "Notifications",
                value = "On",
                onClick = {}
            )
        }
    }
    
}


@Composable
fun SettingItem(
    label: String,
    value: String? = null,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        if (value != null) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}
@Preview
@Composable
fun previewSettings(){
    SynderTheme {
        SettingsScreen()
    }
}