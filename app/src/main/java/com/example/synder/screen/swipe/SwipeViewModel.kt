package com.example.synder.screen.swipe

import android.util.Log
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.synder.models.Coordinates
import com.example.synder.models.UserProfile
import com.example.synder.service.AccountService
import com.example.synder.service.StorageService
import com.example.synder.utilities.GeographicalUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SwipeViewModel @Inject constructor(
    private val storageService: StorageService,
    private val accountService: AccountService,
    private val geographicalUtils: GeographicalUtils
) : ViewModel() {
    val users = MutableStateFlow<List<UserProfile>>(emptyList())
    var currentUserIndex = mutableIntStateOf(0)
    var nextUserIndex = mutableIntStateOf(currentUserIndex.intValue + 1)

    val snackbarHostState = SnackbarHostState()
    var onSnackbarTriggered: (() -> Unit)? = null

    suspend fun getActiveUserCoordinates(): Coordinates? {
        val activeUserId = accountService.currentUserId
        val activeUser = storageService.getUser(activeUserId)
        return activeUser?.let { Coordinates(it.coordinates.latitude, it.coordinates.longitude) }
    }

    suspend fun getCurrentUserCoordinates(): Coordinates? {
        val currentUserId = users.value.getOrNull(currentUserIndex.value)?.id
        return currentUserId?.let { storageService.getUser(it)}?.let { Coordinates(it.coordinates.latitude, it.coordinates.longitude) }
    }

    suspend fun getNextUserCoordinates(): Coordinates? {
        val nextUserId = users.value.getOrNull(nextUserIndex.value)?.id
        return nextUserId?.let { storageService.getUser(it)}?.let { Coordinates(it.coordinates.latitude, it.coordinates.longitude) }
    }

    fun likeUser(targetUserId: String){
        val currentUserId = accountService.currentUserId
        viewModelScope.launch {
            storageService.saveLikedUser(currentUserId, targetUserId)
            checkForMatch(targetUserId)
        }
    }

    fun dislikeUser(targetUserId: String){
        val currentUserId = accountService.currentUserId
        viewModelScope.launch {
            storageService.saveDislikedUser(currentUserId, targetUserId)

        }
    }

    private suspend fun checkForMatch(targetUserId: String){
        val currentUserId = accountService.currentUserId
        val targetUser = storageService.getUser(targetUserId)

        if (targetUser?.likedUsers?.contains(currentUserId) == true){
            Log.d("Matching", "Match found between $currentUserId and $targetUserId")
            storageService.updateMatches(currentUserId, targetUserId)
            onSnackbarTriggered?.invoke()

        }

    }

    private suspend fun getLookingForPreference(): String? {
        val currentUserId = accountService.currentUserId
        val currentUser = storageService.getUser(currentUserId)
        return currentUser?.serEtter

    }

    private suspend fun getUsersLiked(): List<String>? {
        val currentUserId = accountService.currentUserId
        val currentUser = storageService.getUser(currentUserId)
        return currentUser?.likedUsers
    }

    private suspend fun getUsersDisliked(): List<String>? {
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



