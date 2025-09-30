package com.talangraga.umrohmobile.presentation.home.section

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.talangraga.umrohmobile.data.local.database.model.UserEntity
import com.talangraga.umrohmobile.presentation.home.SectionState
import com.talangraga.umrohmobile.ui.TalangragaTypography
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import talangragaumrohmobile.composeapp.generated.resources.Res
import talangragaumrohmobile.composeapp.generated.resources.compose_multiplatform

@Composable
fun ProfileSection(
    modifier: Modifier,
    userType: String?,
    userTypeIcon: ImageVector,
    userTypeShowBottomSheet: Boolean,
    state: SectionState<UserEntity>,
    onShowUserTypeSheet: () -> Unit,
    onRetry: () -> Unit,
    onLogout: () -> Unit // Added onLogout parameter
) {
    val context = LocalPlatformContext.current

    when (state) {
        is SectionState.Loading -> {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Box(
                        modifier = Modifier
                            .height(20.dp)
                            .width(120.dp)
                            .background(Color.LightGray, RoundedCornerShape(4.dp))
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .height(16.dp)
                            .width(80.dp)
                            .background(Color.LightGray, RoundedCornerShape(4.dp))
                    )
                }
            }
        }

        is SectionState.Error -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Failed to load profile", color = Color.Red)
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onRetry) {
                    Text("Retry")
                }
            }
        }

        is SectionState.Success -> {
            val user = state.data
            ConstraintLayout(
                modifier = modifier
                    .background(color = Color.White)
                    .fillMaxWidth()
            ) {
                val (imageProfileRef, nameRef, userTypeRef, logoutButtonRef) = createRefs() // Added logoutButtonRef
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(user.imageProfileUrl)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(Res.drawable.compose_multiplatform),
                    error = painterResource(Res.drawable.compose_multiplatform),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .constrainAs(imageProfileRef) {
                            top.linkTo(parent.top, 16.dp)
                            start.linkTo(parent.start, 16.dp)
                            bottom.linkTo(parent.bottom, 16.dp) // Ensure bottom constraint for centering logout button
                        }
                )

                Text(
                    text = user.fullname,
                    style = TalangragaTypography.bodyMedium,
                    modifier = Modifier.constrainAs(nameRef) {
                        top.linkTo(imageProfileRef.top)
                        bottom.linkTo(userTypeRef.top)
                        start.linkTo(imageProfileRef.end, 8.dp)
                    }
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .constrainAs(userTypeRef) {
                            top.linkTo(nameRef.bottom, 4.dp)
                            start.linkTo(nameRef.start)
                        }
                        .clip(RoundedCornerShape(32.dp))
                        .clickable {
                            onShowUserTypeSheet()
                        }
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF9B59B6),
                                    Color(0xFF6C5CE7)
                                )
                            )
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
//                    Icon(
//                        imageVector = userTypeIcon,
//                        contentDescription = "User Type",
//                        tint = Color.White,
//                        modifier = Modifier.size(20.dp)
//                    )
                    Text(
                        text = userType.orEmpty(),
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                    val rotate by animateFloatAsState(if (userTypeShowBottomSheet) 180f else 0f)
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Expand User Type",
                        tint = Color.White,
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .size(16.dp)
                            .rotate(rotate)
                    )
                }

                // Logout Button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .constrainAs(logoutButtonRef) {
                            top.linkTo(imageProfileRef.top)
                            bottom.linkTo(imageProfileRef.bottom)
                            end.linkTo(parent.end, 16.dp)
                        }
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onLogout() }
                        .background(Color.Red) // Red background
                        .padding(8.dp) // Adjusted padding
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = "Logout",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
//                    Text(
//                        text = "Logout",
//                        color = Color.White,
//                        fontWeight = FontWeight.Normal,
//                        modifier = Modifier.padding(start = 8.dp)
//                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewProfileSection() {
    val dummyUser = UserEntity(
        userId = 1,
        fullname = "John Doe",
        email = "john.doe@example.com",
        phone = "1234567890",
        imageProfileUrl = "https://example.com/profile.jpg",
        userType = "Member",
        userName = "johndoe",
        domisili = "Bandung",
    )
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        ProfileSection(
            modifier = Modifier.fillMaxWidth(),
            userType = "Member",
            userTypeIcon = Icons.Default.Person,
            userTypeShowBottomSheet = false,
            state = SectionState.Success(dummyUser),
            onShowUserTypeSheet = {},
            onRetry = {},
            onLogout = {} // Added for preview
        )
        HorizontalDivider()
        ProfileSection(
            modifier = Modifier.fillMaxWidth(),
            userType = "Admin",
            userTypeIcon = Icons.Default.Person,
            userTypeShowBottomSheet = true,
            state = SectionState.Loading,
            onShowUserTypeSheet = {},
            onRetry = {},
            onLogout = {} // Added for preview
        )
        HorizontalDivider()
        ProfileSection(
            modifier = Modifier.fillMaxWidth(),
            userType = "Member",
            userTypeIcon = Icons.Default.Person,
            userTypeShowBottomSheet = false,
            state = SectionState.Error("Failed to load"),
            onShowUserTypeSheet = {},
            onRetry = {},
            onLogout = {} // Added for preview
        )
    }
}
