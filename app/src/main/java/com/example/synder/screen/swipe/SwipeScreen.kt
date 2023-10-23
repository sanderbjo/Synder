package com.example.synder.screen.swipe

import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.synder.R
import com.example.synder.models.UserProfile
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeScreen() {
    val profiles by remember { mutableStateOf(testprofiles()) }
    var currentIndex by remember { mutableIntStateOf(0) }
    var swipeOffset by remember { mutableIntStateOf(0) }
    val screenWidth = getScreenWidthInt()
    var delayIncrement by remember {mutableStateOf(false) }


    @Composable
    fun likeDislikeButtons(onLike: () -> Unit, onSuperLike: () -> Unit, onDislike: () -> Unit) {
        if (currentIndex < profiles.size) {
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
            targetValue = IntOffset(swipeOffset, 0))

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
                        currentIndex ++

                    } else if (swipeableState.targetValue.toFloat() == 1f){
                        delay(200)
                        currentIndex ++
                    }

                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = userProfile.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .background(MaterialTheme.colorScheme.background)
                            .graphicsLayer {
                                translationX = 0f
                            },
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = userProfile.name, style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Age: ${userProfile.age}", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = userProfile.bio, style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(16.dp))
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
            Box(){
                if (index == currentIndex) {
                    profileCard(
                        userProfile = profiles[index + 1],
                        swipeOffset = 0,
                        modifier = Modifier.zIndex(0f)
                    )
                    profileCard(
                        userProfile = profiles[index + 0],
                        swipeOffset = swipeOffset,
                        modifier = Modifier.zIndex(1f)
                        )

                }

            }
        }
        item {
            Card (
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
                    }
                    ,
                    onSuperLike = {
                        delayIncrement = true
                    })
            }
        }
    }
    LaunchedEffect(delayIncrement){
        if (delayIncrement) {
            delay(200)
            currentIndex ++
            swipeOffset = 0
            delayIncrement = false
        }
    }
}




@Composable
fun getScreenWidthInt(): Int {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current.density
    return (configuration.screenWidthDp * density).toInt()
}



private fun testprofiles(): List<UserProfile> {
    return listOf(
        UserProfile(1, "Katinka", 18, "I love hiking and traveling.", "https://example.com/alice.jpg"),
        UserProfile(2, "Kari", 25, "Passionate about photography.", "https://example.com/bob.jpg"),
        UserProfile(3, "Eline", 28, "Foodie and adventurer.", "https://example.com/charlie.jpg"),
        UserProfile(4, "Marie", 27, "Tech enthusiast.", "https://example.com/david.jpg"),
        UserProfile(4, "Helene", 24, "Tech enthusiast.", "https://example.com/david.jpg"),
        UserProfile(4, "Josefine", 19, "Tech enthusiast.", "https://example.com/david.jpg"),
        UserProfile(4, "Sandra", 20, "Tech enthusiast.", "https://example.com/david.jpg"),
        UserProfile(4, "Hedda", 25, "Tech enthusiast.", "https://example.com/david.jpg"),
        UserProfile(4, "Charlotte", 25, "Tech enthusiast.", "https://example.com/david.jpg"),
        UserProfile(4, "Caroline", 21, "Tech enthusiast.", "https://example.com/david.jpg"),
        UserProfile(4, "Kristine", 24, "Tech enthusiast.", "https://example.com/david.jpg"),
        UserProfile(4, "Elisa", 18, "Tech enthusiast.", "https://example.com/david.jpg"),
    )

}
@Composable
@Preview
fun Previewpage() {
    SwipeScreen()
}
