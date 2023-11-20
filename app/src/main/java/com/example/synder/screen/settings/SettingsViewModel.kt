package com.example.synder.screen.settings

import android.app.Activity
import android.content.Context
import android.media.AudioManager
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.synder.service.AccountService
import com.example.synder.service.StorageService
import com.example.synder.utils.LocationUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val storageService: StorageService,
    private val accountService: AccountService,
    private val context : Context,
    private val locationUtils: LocationUtils) : ViewModel(){
    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    private val _volumeLevel = mutableIntStateOf(getMediaVolume())
    val volumeLevel: State<Int> = _volumeLevel

    suspend fun requestLocationPermission(activity: Activity, requestCode: Int){
        viewModelScope.launch {
            locationUtils.requestLocationPermission(activity, requestCode)
            delay(10000)
            updateLocationIfPermissionIsGranted()
        }

    }

    suspend fun updateLocationIfPermissionIsGranted(){
        val userid = accountService.currentUserId
        if (locationUtils.checkLocationPermission()) {
            locationUtils.getCurrentLocation(
                onLocationResult = { location ->
                    viewModelScope.launch {
                        storageService.updateUserLocation(
                            userid,
                            location.latitude,
                            location.longitude
                        )
                    }
                },
                onFailure = { exception ->
                    Log.e("SettingsViewModel", "Failed to get location ${exception.message}")
                }
            )
        }
    }

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