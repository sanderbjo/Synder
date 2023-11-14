package com.example.synder.screen.swipe

import android.util.Log
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.synder.models.UserProfile
import com.example.synder.service.AccountService
import com.example.synder.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SwipeViewModel @Inject constructor(
    private val storageService: StorageService,
    private val accountService: AccountService
) : ViewModel() {
    val users = MutableStateFlow<List<UserProfile>>(emptyList())
    var currentUserIndex = mutableIntStateOf(0)
    var nextUserIndex = mutableIntStateOf(currentUserIndex.value + 1)

    val snackbarHostState = SnackbarHostState()
    var onSnackbarTriggered: (() -> Unit)? = null




    fun likeUser(targetUserId: String){
        val currentUserId = accountService.currentUserId
        if (currentUserId != null){
            viewModelScope.launch {
                storageService.saveLikedUser(currentUserId, targetUserId)
                checkForMatch(targetUserId)
            }
        }
    }

    fun dislikeUser(targetUserId: String){
        val currentUserId = accountService.currentUserId
        if (currentUserId != null){
            viewModelScope.launch {
                storageService.saveDislikedUser(currentUserId, targetUserId)

            }

        }
    }



    /*suspend fun triggerSnackbar(){
        snackbarHostState.showSnackbar(
            message = "Du har matchet med en Synder!",
            duration = SnackbarDuration.Short
        )
    }*/

    suspend fun checkForMatch(targetUserId: String){
        val currentUserId = accountService.currentUserId
        val targetUser = storageService.getUser(targetUserId)

        if (targetUser?.likedUsers?.contains(currentUserId) == true){
            Log.d("Matching", "Match found between $currentUserId and $targetUserId")
            storageService.updateMatches(currentUserId, targetUserId)
            onSnackbarTriggered?.invoke()

        }

    }

    suspend fun getLookingForPreference(): String? {
        val currentUserId = accountService.currentUserId
        val currentUser = storageService.getUser(currentUserId)
        return currentUser?.serEtter

    }

    suspend fun getUsersLiked(): List<String>? {
        val currentUserId = accountService.currentUserId
        val currentUser = storageService.getUser(currentUserId)
        return currentUser?.likedUsers
    }

    suspend fun getUsersDisliked(): List<String>? {
        val currentUserId = accountService.currentUserId
        val currentUser = storageService.getUser(currentUserId)
        return currentUser?.dislikedUsers
    }



    init {
        viewModelScope.launch {
            val lookingForPreference = getLookingForPreference()
            val likedUsers = getUsersLiked() ?: emptyList()
            val dislikedUsers = getUsersDisliked() ?: emptyList()
            storageService.users.collect { profiles ->
                val filteredAndSortedProfiles = profiles
                    .filter { profile ->
                        profile.kjonn == lookingForPreference /*&&
                                profile.id !in likedUsers &&
                                profile.id !in dislikedUsers                Add this to implement filter based on people liked/disliked*/
                    }
                users.value = filteredAndSortedProfiles

            }

        }
    }

    }



