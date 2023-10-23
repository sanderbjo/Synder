package com.example.synder.screen.profile


import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.synder.ui.theme.SynderTheme
import com.example.synder.R

@Composable
fun ProfileScreen(modifier: Modifier = Modifier,
                  viewModel: ProfileViewModel = hiltViewModel()
) {
    val user by viewModel.user
    /*val images = listOf(
        painterResource(id = R.drawable.ic_launcher_foreground),
        painterResource(id = R.drawable.ic_launcher_foreground),
        painterResource(id = R.drawable.ic_launcher_foreground),
        painterResource(id = R.drawable.ic_launcher_foreground),
        painterResource(id = R.drawable.ic_launcher_foreground)
    )*/
    Column(
        modifier
            .fillMaxSize()
            .padding(12.dp)) {
        /*LazyRow {
            items(images) {image ->
                Image(painter = image,
                    contentDescription = "profilePicture",
                    modifier = Modifier
                        .border(BorderStroke(1.dp, Color.Black))
                        .height(180.dp)
                        .width(120.dp))
            }
        }*/
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(user.profileImageUrl)
                .crossfade(true)
                .build(),
            contentDescription = "profilbilde",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
        )

        Card(modifier.padding(12.dp)) {
            Column(modifier.padding(12.dp,), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = user.name + " - " + user.age + " år",Modifier.padding(top = 12.dp))
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = user.bio)
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
        Row(modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center) {
            Button(onClick = { /*TODO*/ },
                Modifier
                    .width(160.dp)
                    .height(60.dp)) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Rediger bilder")
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
        Row(Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween) {
            Button(onClick = { /*TODO*/ },
                Modifier
                    .width(160.dp)
                    .height(60.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Innstillinger")
            }
            Button(onClick = { /*TODO*/ },
                Modifier
                    .width(160.dp)
                    .height(60.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Personlig informasjon")
            }
        }
        /*Row(
            Modifier
                .fillMaxSize()
                .padding(top = 12.dp) ,horizontalArrangement = Arrangement.SpaceAround) {


            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null
                    )
                }
                Text(text = "Edit bio")
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Default.Face,
                        contentDescription = null

                    )
                }
                Text(text = "Edit images")
            }
        }*/
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewProfile(){
    SynderTheme {
        ProfileScreen()
    }
}