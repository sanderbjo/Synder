package com.example.synder.screen.swipe

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.synder.R
import com.example.synder.models.UserProfile
import com.example.synder.utilities.GeographicalUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class, ExperimentalGlideComposeApi::class)
@Composable
fun SwipeScreen(viewModel: SwipeViewModel = hiltViewModel(), context: Context) {

    var currentUserIndex by viewModel.currentUserIndex
    var nextValueIndex by viewModel.nextUserIndex
    val profiles by viewModel.users.collectAsState()
    val currentUser = profiles.getOrNull(currentUserIndex)
    val nextUser = profiles.getOrNull(nextValueIndex)
    var swipeOffsetX by remember { mutableIntStateOf(0) }
    var swipeOffsetY by remember { mutableIntStateOf(0) }
    val screenWidth = getScreenWidthInt()
    val screenHeight = getScreenHeightInt()
    var delayIncrement by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = viewModel.snackbarHostState
    val storageRef = viewModel.storageRef
    viewModel.onSnackbarTriggered = { scope.launch {
            triggerSnackbar(snackbarHostState = snackbarHostState, context = context) }}



    @Composable
    fun likeDislikeButtons(onLike: () -> Unit, onSuperLike: () -> Unit, onDislike: () -> Unit) {
        if (currentUserIndex < profiles.size) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(
                    onClick = {
                        onDislike()
                        swipeOffsetX = -screenWidth
                        swipeOffsetY = 50
                    },
                    modifier = Modifier.size(50.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear, contentDescription = stringResource(R.string.dislike),
                        modifier = Modifier.size(50.dp)
                    )
                }
                Spacer(modifier = Modifier.width(32.dp))

                IconButton(
                    onClick = {
                        onSuperLike()
                        swipeOffsetY = -screenHeight
                    },
                    modifier = Modifier.size(50.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star, contentDescription = stringResource(R.string.super_like),
                        modifier = Modifier.size(50.dp)
                    )
                }
                Spacer(modifier = Modifier.width(32.dp))

                IconButton(
                    onClick = {
                        onLike()
                        swipeOffsetX = screenWidth
                        swipeOffsetY = 50
                    },
                    modifier = Modifier.size(50.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite, contentDescription = stringResource(R.string.like),
                        modifier = Modifier.size(50.dp)
                    )
                }
            }
        }
    }

    @Composable
    fun profileCard(userProfile: UserProfile, swipeOffsetX: Int, swipeOffsetY: Int, modifier: Modifier) {
        val offsetState by animateIntOffsetAsState(
            targetValue = IntOffset(swipeOffsetX, swipeOffsetY),
            animationSpec = TweenSpec(durationMillis = 250), label = stringResource(R.string.animation_label)
        )
        val swipeableState = rememberSwipeableState(0)
        val sizePx = with(LocalDensity.current) { screenWidth.dp.toPx() }
        val anchors = mapOf(0f to 0, -sizePx to 1, sizePx to -1)


        val distanceToCurrentUser by produceState<Int?>(null){
            value = viewModel.getActiveUserCoordinates()
                ?.let { GeographicalUtils.calculateDistance(it,
                    viewModel.getCurrentUserCoordinates()!!
                ) }
        }

        val distanceToNextUser by produceState<Int?>(0){
            value = viewModel.getActiveUserCoordinates()
                ?.let { GeographicalUtils.calculateDistance(it,
                    viewModel.getNextUserCoordinates()!!
                ) }
        }

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .padding(16.dp)
                .offset { offsetState }
                .swipeable(
                    state = swipeableState,
                    anchors = anchors,
                    thresholds = { _, _ -> FractionalThreshold(0.3f) },
                    orientation = Orientation.Horizontal
                )
        )
        {
            Card(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(16.dp)
                    .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) },

            ) {
                LaunchedEffect(swipeableState.targetValue) {
                    if (swipeableState.targetValue.toFloat() == -1f) {
                        currentUser?.id?.let { viewModel.likeUser(it) }
                        delay(200)
                        currentUserIndex++
                        nextValueIndex++


                    } else if (swipeableState.targetValue.toFloat() == 1f) {
                        currentUser?.id?.let { viewModel.dislikeUser(it) }
                        delay(200)
                        currentUserIndex++
                        nextValueIndex++
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    if (userProfile == profiles[currentUserIndex]){
                        GlideImage(
                            model = storageRef.child("images/${currentUser?.id}.jpg"),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .background(MaterialTheme.colorScheme.background)
                                .graphicsLayer {
                                    translationX = 0f
                                }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row {
                            currentUser?.name?.let {
                                Text(
                                    text = stringResource(R.string.it_with_gap, it),
                                    style = MaterialTheme.typography.headlineSmall
                                )
                            }
                            currentUser?.age?.let {
                                Text(
                                    text = it,
                                    style = MaterialTheme.typography.headlineSmall
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = stringResource(
                            R.string.currentUser_km_unna_deg,
                            distanceToCurrentUser.toString()
                        ))
                        Spacer(modifier = Modifier.height(8.dp))
                        currentUser?.bio?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    else if (userProfile == profiles[nextValueIndex]){
                        GlideImage(
                            model = storageRef.child("images/${nextUser?.id}.jpg"),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .background(MaterialTheme.colorScheme.background)
                                .graphicsLayer {
                                    translationX = 0f
                                }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row {
                            nextUser?.name?.let {
                                Text(
                                    text = stringResource(R.string.it_with_gap, it),
                                    style = MaterialTheme.typography.headlineSmall
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            nextUser?.age?.let {
                                Text(
                                    text = it,
                                    style = MaterialTheme.typography.headlineSmall
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = stringResource(
                            R.string.km_unna_deg,
                            distanceToNextUser.toString()
                        ))
                        Spacer(modifier = Modifier.height(8.dp))
                        nextUser?.bio?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }

    if (profiles.isNotEmpty()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = { SnackbarHost(hostState = snackbarHostState)}
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                items(profiles.size) { index ->
                    Box {
                        if (index == currentUserIndex && index <= profiles.size) {

                            if (index <= profiles.size - 2) {
                                profileCard(
                                    userProfile = profiles[nextValueIndex],
                                    swipeOffsetX = 0,
                                    swipeOffsetY = 0,
                                    modifier = Modifier.zIndex(0f)
                                )
                            }
                            profileCard(
                                userProfile = profiles[currentUserIndex],
                                swipeOffsetX = swipeOffsetX,
                                swipeOffsetY = swipeOffsetY,
                                modifier = Modifier.zIndex(1f)
                            )

                            if (index >= profiles.size - 1) {
                                nextValueIndex = 0
                            }
                        }
                    }
                }
                item {
                    if (currentUserIndex == profiles.size) {
                        endOfListCard()
                    }
                }

                item {
                    Card(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth()
                            .padding(16.dp),
                    ) {
                        likeDislikeButtons(
                            onLike = {
                                currentUser?.id?.let { viewModel.likeUser(it) }
                                delayIncrement = true
                            },

                            onDislike = {
                                currentUser?.id?.let { viewModel.dislikeUser(it) }
                                delayIncrement = true
                            },
                            onSuperLike = {
                                currentUser?.id?.let { viewModel.likeUser(it) }
                                delayIncrement = true
                            })
                    }
                }
            }
        }
    } else if (profiles.isEmpty()){
        endOfListCard()
    }

    LaunchedEffect(delayIncrement){
        if (delayIncrement) {
            delay(200)
            currentUserIndex ++
            nextValueIndex++

            swipeOffsetX = 0
            swipeOffsetY = 0
            delayIncrement = false
        }
    }
}


@Composable
fun endOfListCard(){
    Card(
        modifier = Modifier
            .padding(16.dp)
    ) {
        Column (
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Icon(
                imageVector = Icons.Default.Face,
                contentDescription = stringResource(R.string.face_icon),
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.du_har_swipet_gjennom_alle_personer_pr_v_igjen_senere),
                textAlign = TextAlign.Center
            )

        }
    }

}

suspend fun triggerSnackbar(snackbarHostState: SnackbarHostState, context: Context){
    snackbarHostState.showSnackbar(
        message = context.getString(R.string.du_har_matchet_med_en_synder),
        duration = SnackbarDuration.Short
    )
}

@Composable
fun getScreenWidthInt(): Int {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current.density
    return (configuration.screenWidthDp * density).toInt()
}

@Composable
fun getScreenHeightInt(): Int {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current.density
    return (configuration.screenHeightDp * density).toInt()
}
