package com.example.synder.screen.swipe

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.synder.models.UserProfile
import com.example.synder.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SwipeViewModel @Inject constructor(
    private val storageService: StorageService)
    : ViewModel(){
     val user = mutableStateOf(UserProfile())

    init {
        viewModelScope.launch {
            val userProfileList = storageService.users.first()

            if (userProfileList.isNotEmpty()) {
                val firstUserProfile = userProfileList[0]

                user.value = firstUserProfile
            } else {
                println("No user profiles found")
            }
        }
    }

    }