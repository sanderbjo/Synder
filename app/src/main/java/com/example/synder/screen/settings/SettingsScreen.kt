package com.example.synder.screen.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment



@Composable
fun SettingsScreen(isDarkTheme: Boolean, toggleTheme: () -> Unit){
    val viewModel: SettingsViewModel = hiltViewModel()


    val volumeLevel by viewModel.volumeLevel
    LazyColumn(){
        item{
            SwitchSettingItem(
                label = "App Theme",
                value = if (isDarkTheme) "Dark" else "Light",
                switchState = isDarkTheme,
                onSwitchStateChanged = { newSwitchState -> toggleTheme() }

            )
        }
        item{
            SettingItem(
                label = "Notifications",
                value = "On",
                onClick = {}
            )
        }
        item{
            VolumeSettingItem(
                label = "Volume",
                volumeLevel = volumeLevel,
                onVolumeChanged = {newVolumeLevel ->
                    viewModel.setVolumeLevel(newVolumeLevel)
                })
        }




    }

}

@Composable
fun SwitchSettingItem(
    label: String,
    value: String? = null,
    switchState: Boolean,
    onSwitchStateChanged: (Boolean) -> Unit
) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ){
        Column(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)

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
        Switch(
            checked = switchState,
            onCheckedChange = { newSwitchState ->
                onSwitchStateChanged(newSwitchState)
            }
        )
    }
}

@Composable
fun VolumeSettingItem(
    label: String,
    volumeLevel: Int,
    onVolumeChanged: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Volume Level: $volumeLevel%",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.width(16.dp))
            Slider(
                value = volumeLevel / 100f,
                onValueChange = { newVolume ->
                    val newVolumeLevel = (newVolume * 100).toInt()
                    onVolumeChanged(newVolumeLevel)
                },
                valueRange = 0f..1f,
                steps = 5
            )
        }
    }
}

@Composable
fun SettingItem(
    label: String,
    value: String? = null,
    onClick: () -> Unit,
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

/*@Preview
@Composable
fun previewSettings(){
    SynderTheme {
        SettingsScreen()
    }*/
