package com.example.synder.screen.profile

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.synder.models.UserProfile
import com.example.synder.service.AccountService
import com.example.synder.service.ImgStorageService
import com.example.synder.service.StorageService
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val storageService: StorageService,
    private val accountService: AccountService,
    private val imgStorageService: ImgStorageService
    ) : ViewModel() {
    val user = mutableStateOf(UserProfile())
    var userImgRef = mutableStateOf<StorageReference?>(null)
    val storageRef = mutableStateOf<StorageReference?>(null)
    init {
        val userId = accountService.currentUserId
        viewModelScope.launch {
            user.value = storageService.getUser(userId) ?: UserProfile()
            userImgRef.value = imgStorageService.getImgRef(userId)
            storageRef.value = imgStorageService.storageRef
        }
    }

}