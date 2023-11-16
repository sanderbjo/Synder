package com.example.synder.screen.settings

import android.content.Context
import android.media.AudioManager
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(private val context : Context) : ViewModel(){
    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    private val _volumeLevel = mutableIntStateOf(getMediaVolume())
    val volumeLevel: State<Int> = _volumeLevel

    fun setVolumeLevel(newVolumeLevel: Int){
        _volumeLevel.value = newVolumeLevel
        setMediaVolume(newVolumeLevel)
    }


    private fun setMediaVolume(volumelevel: Int){
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val scaledVolume = (volumelevel * maxVolume) / 100
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, scaledVolume, 0)
    }
    fun getMediaVolume(): Int {
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        return (currentVolume * 100) / maxVolume
    }

}