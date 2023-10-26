package com.example.synder.screen.swipe

import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.synder.models.UserProfile
import com.example.synder.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class SwipeViewModel @Inject constructor(
    private val storageService: StorageService)
    : ViewModel() {
    val users = MutableStateFlow<List<UserProfile>>(emptyList())
    var currentUserIndex = mutableIntStateOf(0)
    var nextUserIndex = mutableIntStateOf(0)




    init {
        viewModelScope.launch {

            storageService.users.collect {
                users.value = it
                if (currentUserIndex.value >= it.size){
                    currentUserIndex.value = 0
                }
            }

        }
    }

    }



