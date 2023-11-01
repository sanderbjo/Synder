package com.example.synder.screen.settings

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class SettingsViewModel : ViewModel(){
    private val _volumeLevel = mutableIntStateOf(50)
    val volumeLevel: State<Int> = _volumeLevel

    fun setVolumeLevel(newVolumeLevel: Int){
        _volumeLevel.value = newVolumeLevel
    }

}