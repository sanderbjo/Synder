package com.example.synder.screen.profile

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.synder.models.UserProfile
import com.example.synder.service.AccountService
import com.example.synder.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val storageService: StorageService,
    private val accountService: AccountService
    ) : ViewModel() {
    val user = mutableStateOf(UserProfile())
    //val userId = accountService.currentUser
    init {
        val userId = accountService.currentUserId
        viewModelScope.launch {
            user.value = storageService.getUser(userId) ?: UserProfile()
        }
    }
}