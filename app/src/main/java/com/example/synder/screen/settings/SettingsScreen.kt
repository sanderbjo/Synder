package com.example.synder.screen.settings

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.synder.R
import kotlinx.coroutines.launch


@Composable
fun SettingsScreen(
    isDarkTheme: Boolean,
    toggleTheme: () -> Unit,
    context: Context,
    signedOut: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
){

    val scope = rememberCoroutineScope()
    val checkPermission by remember {mutableStateOf(viewModel.checkPermission)}

    val volumeLevel by remember { mutableStateOf(viewModel.volumeLevel).value }
    LazyColumn(){
        item{
            SwitchSettingItem(
                label = stringResource(R.string.app_tema),
                value = if (isDarkTheme) stringResource(R.string.m_rk) else stringResource(R.string.lys),
                switchState = isDarkTheme,
                onSwitchStateChanged = { newSwitchState -> toggleTheme() }

            )
        }
        item{
            VolumeSettingItem(
                label = stringResource(R.string.volum),
                volumeLevel = volumeLevel,
                onVolumeChanged = {newVolumeLevel ->
                    viewModel.setVolumeLevel(newVolumeLevel)
                })
        }
        item {
            CheckBoxSettingItem(

                label = stringResource(R.string.godta_gps_lokasjon),
                checked = checkPermission.value,
                onCheckedChange = {newCheckedState ->
                    scope.launch {
                        if (newCheckedState){
                            val requestCode = 123
                            viewModel.requestLocationPermission(context as Activity, requestCode)
                            checkPermission.value = true

                        }
                    }
                })
        }
        item {
            SignOutButton(signedOut)
        }
    }


}


@Composable
fun CheckBoxSettingItem(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        ) {
            Text(text = label, style = MaterialTheme.typography.bodyMedium)
        }
        Checkbox(
            checked = checked,
            onCheckedChange = { newCheckedState ->
                onCheckedChange(newCheckedState)
            }
        )
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
                    style = MaterialTheme.typography.bodySmall
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
                text = "Volum level: $volumeLevel%",
                style = MaterialTheme.typography.bodySmall
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
fun SignOutButton(signedOut: () -> Unit, viewModel: SettingsViewModel = hiltViewModel()) {
    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
        Button(
            onClick = { viewModel.signOutClick(signedOut = signedOut) },
            modifier = Modifier
                .padding(16.dp, 8.dp)
        ) {
            Text(text = stringResource(R.string.logg_ut), fontSize = 16.sp)
        }
    }
}

