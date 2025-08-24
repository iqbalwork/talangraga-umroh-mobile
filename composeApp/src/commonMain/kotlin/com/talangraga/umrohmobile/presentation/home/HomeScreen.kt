package com.talangraga.umrohmobile.presentation.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.talangraga.umrohmobile.data.local.database.model.UserEntity
import com.talangraga.umrohmobile.presentation.navigation.HomeRoute
import com.talangraga.umrohmobile.presentation.navigation.LoginRoute
import com.talangraga.umrohmobile.ui.TalangragaTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import talangragaumrohmobile.composeapp.generated.resources.Res
import talangragaumrohmobile.composeapp.generated.resources.compose_multiplatform

@Composable
fun HomeScreen(
    navHostController: NavHostController,
    justLogin: Boolean,
    viewModel: HomeViewModel = koinViewModel()
) {

    val profile by viewModel.profile.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()

    if (justLogin) {
        viewModel.getProfile()
    } else {
        viewModel.getLocalProfile()
    }

    HomeContent(
        profile = profile
    ) {
        viewModel.clearSession()
        navHostController.navigate(LoginRoute) {
            popUpTo(HomeRoute()) {
                inclusive = true
            }
        }
    }
}

@Composable
fun HomeContent(
    profile: UserEntity?,
    onLogout: () -> Unit
) {

    val context = LocalPlatformContext.current

    Scaffold { paddingValues ->
        ConstraintLayout(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {

            val (imageProfileRef, nameRef) = createRefs()

            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(profile?.imageProfileUrl)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(Res.drawable.compose_multiplatform),
                error = painterResource(Res.drawable.compose_multiplatform),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .constrainAs(imageProfileRef) {
                        top.linkTo(parent.top, 16.dp)
                        start.linkTo(parent.start, 16.dp)
                    }
            )

            Text(
                text = "Hi ${profile?.fullname}",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.constrainAs(nameRef) {
                    top.linkTo(imageProfileRef.top)
                    start.linkTo(imageProfileRef.end, 8.dp)
                }
            )

            Button(onClick = onLogout, modifier = Modifier.constrainAs(createRef()) {
                top.linkTo(imageProfileRef.bottom, 16.dp)
                start.linkTo(parent.start, 16.dp)
                end.linkTo(parent.end, 16.dp)
                width = Dimension.fillToConstraints
            }) {
                Text("Logout")
            }
        }
    }
}

@Preview
@Composable
fun HomeContentPreview() {
    TalangragaTheme {
        HomeContent(null) {

        }
    }
}