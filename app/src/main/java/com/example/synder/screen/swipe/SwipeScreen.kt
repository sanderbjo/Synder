package com.example.synder.screen.swipe

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.synder.models.UserProfile
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeScreen(viewModel: SwipeViewModel = hiltViewModel()) {

    var currentUserIndex by viewModel.currentUserIndex
    var nextValueIndex by viewModel.nextUserIndex

    val profiles by viewModel.users.collectAsState()
    val currentUser = profiles.getOrNull(currentUserIndex)
    val nextUser = profiles.getOrNull(nextValueIndex)

    var swipeOffset by remember { mutableIntStateOf(0) }
    val screenWidth = getScreenWidthInt()
    var delayIncrement by remember {mutableStateOf(false) }







    @Composable
    fun likeDislikeButtons(onLike: () -> Unit, onSuperLike: () -> Unit, onDislike: () -> Unit) {
        if (currentUserIndex < profiles.size) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(
                    onClick = {
                        onDislike()
                        swipeOffset = -screenWidth},
                    modifier = Modifier.size(50.dp)
                ) {
                    Icon(imageVector = Icons.Default.Clear, contentDescription = "Dislike",
                        modifier = Modifier.size(50.dp))
                }
                Spacer(modifier = Modifier.width(32.dp))

                IconButton(
                    onClick = {
                        onSuperLike()
                        swipeOffset = screenWidth},
                    modifier = Modifier.size(50.dp)
                ) {
                    Icon(imageVector = Icons.Default.Star, contentDescription = "Super Like",
                        modifier = Modifier.size(50.dp))
                }
                Spacer(modifier = Modifier.width(32.dp))

                IconButton(
                    onClick = {
                        onLike()
                        swipeOffset = screenWidth},
                    modifier = Modifier.size(50.dp)
                ) {
                    Icon(imageVector = Icons.Default.Favorite, contentDescription = "Like",
                        modifier = Modifier.size(50.dp))
                }
            }
        }
    }

    @Composable
    fun profileCard(userProfile: UserProfile, swipeOffset: Int, modifier: Modifier) {
        val offsetXState by animateIntOffsetAsState(
            targetValue = IntOffset(swipeOffset, 0),
            animationSpec = TweenSpec(durationMillis = 250))
        val swipeableState = rememberSwipeableState(0)
        val sizePx = with(LocalDensity.current) { screenWidth.dp.toPx() }
        val anchors = mapOf(0f to 0, -sizePx to 1, sizePx to -1)

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .padding(16.dp)
                .offset { offsetXState }
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
                    .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }

                ) {
                LaunchedEffect(swipeableState.targetValue){
                    if (swipeableState.targetValue.toFloat() == -1f){
                        delay(200)
                        currentUserIndex ++


                    } else if (swipeableState.targetValue.toFloat() == 1f){
                        delay(200)
                        currentUserIndex ++
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    if (userProfile == profiles[currentUserIndex]){
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(currentUser?.profileImageUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = "profilbilde",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .background(MaterialTheme.colorScheme.background)
                                .graphicsLayer {
                                    translationX = 0f
                                })
                        Spacer(modifier = Modifier.height(16.dp))
                        currentUser?.name?.let { Text(text = it, style = MaterialTheme.typography.headlineSmall) }
                        Spacer(modifier = Modifier.height(8.dp))
                        currentUser?.age?.let { Text(text = it, style = MaterialTheme.typography.bodyMedium) }
                        Spacer(modifier = Modifier.height(8.dp))
                        currentUser?.bio?.let { Text(text = it, style = MaterialTheme.typography.bodyMedium) }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    else if (userProfile == profiles[nextValueIndex]){
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(nextUser?.profileImageUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = "profilbilde",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .background(MaterialTheme.colorScheme.background)
                                .graphicsLayer {
                                    translationX = 0f
                                })
                        Spacer(modifier = Modifier.height(16.dp))
                        nextUser?.name?.let { Text(text = it, style = MaterialTheme.typography.headlineSmall) }
                        Spacer(modifier = Modifier.height(8.dp))
                        nextUser?.age?.let { Text(text = it, style = MaterialTheme.typography.bodyMedium) }
                        Spacer(modifier = Modifier.height(8.dp))
                        nextUser?.bio?.let { Text(text = it, style = MaterialTheme.typography.bodyMedium) }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(profiles.size) { index ->
            Box() {

                if (index == currentUserIndex) {
                    profileCard(
                        userProfile = profiles[nextValueIndex],
                        swipeOffset = 0,
                        modifier = Modifier.zIndex(0f)
                    )

                    profileCard(
                        userProfile = profiles[currentUserIndex],
                        swipeOffset = swipeOffset,
                        modifier = Modifier.zIndex(1f)
                    )


                }

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
                        delayIncrement = true

                    },

                    onDislike = {
                        delayIncrement = true
                    },
                    onSuperLike = {
                        delayIncrement = true
                    })
            }
        }

    }
    LaunchedEffect(delayIncrement){
        if (delayIncrement) {
            delay(200)
            currentUserIndex ++

            swipeOffset = 0
            delayIncrement = false
        }
    }

    LaunchedEffect(currentUserIndex){
        nextValueIndex++
    }
}


@Composable
fun getScreenWidthInt(): Int {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current.density
    return (configuration.screenWidthDp * density).toInt()
}


@Composable
@Preview
fun Previewpage() {
    SwipeScreen()
}
