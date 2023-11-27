package com.example.synder.screen.profile


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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.synder.R
import com.example.synder.Screen


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProfileScreen(modifier: Modifier = Modifier,
                  viewModel: ProfileViewModel = hiltViewModel(), navController: NavHostController
) {
    val user by viewModel.user
    val userImgRef by viewModel.userImgRef
    val storageRef by viewModel.storageRef

    Column(
        modifier
            .fillMaxSize()
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GlideImage(
            model = storageRef?.child("images/${user.id}.jpg"),
            contentDescription = stringResource(R.string.profilbilde),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
                .fillMaxWidth()

        )
        Card(
            modifier
                .padding(12.dp)
                .fillMaxWidth(0.9f)) {
            Column(
                modifier
                    .padding(12.dp)
                    .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = user.name + " - " + user.age + " år",)
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
                    contentDescription = stringResource(R.string.edit_bilde)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = stringResource(R.string.rediger_bilder))
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
        Row(Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween) {
            Button(onClick = { navController.navigate(Screen.Settings.name) },
                Modifier
                    .width(160.dp)
                    .height(60.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = stringResource(R.string.settings)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = stringResource(R.string.innstillinger))
            }
            Button(onClick = { /*TODO*/ },
                Modifier
                    .width(160.dp)
                    .height(60.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.edit_profil)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = stringResource(R.string.personlig_informasjon))
            }
        }
    }
}
