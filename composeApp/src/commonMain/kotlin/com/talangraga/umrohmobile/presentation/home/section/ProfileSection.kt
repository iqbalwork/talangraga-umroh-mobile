package com.talangraga.umrohmobile.presentation.home.section

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
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.talangraga.shared.TalangragaTypography
import com.talangraga.umrohmobile.presentation.home.SectionState
import com.talangraga.umrohmobile.presentation.user.model.UserUIData
import com.talangraga.umrohmobile.ui.component.BasicImage
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ProfileSection(
    modifier: Modifier,
    userType: String?,
    state: SectionState<UserUIData>,
    onClickImage: (String) -> Unit = {},
    onRetry: () -> Unit,
    onLogout: () -> Unit
) {

    when (state) {
        is SectionState.Loading -> {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
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
                    .padding(horizontal = 16.dp, vertical = 8.dp),
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
                modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                val (imageProfileRef, nameRef, userTypeRef, listUserRef, settingsRef, logoutButtonRef) = createRefs()
                BasicImage(
                    model = user.imageProfileUrl,
                    modifier = Modifier
                        .clickable { onClickImage(user.imageProfileUrl) }
                        .size(60.dp)
                        .clip(CircleShape)
                        .constrainAs(imageProfileRef) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            bottom.linkTo(parent.bottom)
                        }
                )

                Text(
                    text = user.fullname,
                    style = TalangragaTypography.bodyMedium,
                    modifier = Modifier
                        .constrainAs(nameRef) {
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
                            bottom.linkTo(imageProfileRef.bottom)
                            start.linkTo(nameRef.start)
                        }
                        .clip(RoundedCornerShape(32.dp))
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
                    Text(
                        text = userType?.replaceFirstChar { it.titlecase() }.orEmpty(),
                        style = TalangragaTypography.bodySmall.copy(
                            textAlign = TextAlign.Center,
                            color = Color.White
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }

                // Logout Button
                Box(
                    modifier = Modifier
                        .constrainAs(logoutButtonRef) {
                            top.linkTo(imageProfileRef.top)
                            bottom.linkTo(imageProfileRef.bottom)
                            end.linkTo(parent.end)
                        }
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onLogout() }
                        .background(Color.Red) // Red background
                        .padding(4.dp) // Adjusted padding
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = "Logout",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewProfileSection() {
    val dummyUser = UserUIData(
        id = 1,
        fullname = "John Doe",
        email = "john.doe@example.com",
        phone = "1234567890",
        imageProfileUrl = "https://example.com/profile.jpg",
        userType = "Member",
        username = "johndoe",
        domicile = "Bandung",
        isActive = true,
    )
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        ProfileSection(
            modifier = Modifier.fillMaxWidth(),
            userType = "Member",
            state = SectionState.Success(dummyUser),
            onRetry = {},
            onLogout = {},
        )
        HorizontalDivider()
        ProfileSection(
            modifier = Modifier.fillMaxWidth(),
            userType = "Admin",
            state = SectionState.Loading,
            onRetry = {},
            onLogout = {},
        )
        HorizontalDivider()
        ProfileSection(
            modifier = Modifier.fillMaxWidth(),
            userType = "Member",
            state = SectionState.Error("Failed to load"),
            onRetry = {},
            onLogout = {},
        )
    }
}
