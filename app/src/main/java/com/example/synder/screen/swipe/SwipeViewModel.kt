package com.example.synder.screen.swipe

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.synder.models.UserProfile
import com.example.synder.service.ImgStorageService
import com.example.synder.service.StorageService
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class SwipeViewModel @Inject constructor(
    private val storageService: StorageService,
    private val imgStorageService: ImgStorageService
)
    : ViewModel() {
    val users = MutableStateFlow<List<UserProfile>>(emptyList())
    var currentUserIndex = mutableIntStateOf(0)
    var nextUserIndex = mutableIntStateOf(0)

    var storageRef = imgStorageService.storageRef


    init {
        viewModelScope.launch {

            storageService.users.collect {
                users.value = it
                if (currentUserIndex.value >= it.size){
                    currentUserIndex.value = 0
                }
            }
            //imgStorageService.storageRef
            //storageRef.value = imgStorageService.getStorageRef()

        }
    }

    }



