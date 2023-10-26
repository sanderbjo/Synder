package com.example.synder.screen.swipe

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.synder.models.UserProfile
import com.example.synder.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class SwipeViewModel @Inject constructor(
    private val storageService: StorageService)
    : ViewModel() {
    val users = MutableStateFlow<List<UserProfile>>(emptyList())
    val currentUserIndex = mutableIntStateOf(0)




    init {
        viewModelScope.launch {

            storageService.users.collect {
                users.value = it
                if (currentUserIndex.intValue >= it.size){
                    currentUserIndex.intValue = 0
                }


            }

        }
    }
}

