package com.talangraga.umrohmobile.presentation.user.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.talangraga.umrohmobile.presentation.user.model.UserUIData
import com.talangraga.shared.Sage
import com.talangraga.umrohmobile.ui.TalangragaTheme
import com.talangraga.shared.TalangragaTypography
import com.talangraga.umrohmobile.ui.ThemeManager
import com.talangraga.umrohmobile.ui.ThemeMode
import com.talangraga.umrohmobile.ui.component.BasicImage
import com.talangraga.umrohmobile.ui.component.IconBlock
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreen(
    navHostController: NavHostController,
    user: UserUIData?,
    isLoginUser: Boolean,
    viewModel: ProfileViewModel = koinViewModel(),
) {

    val profile by viewModel.profile.collectAsStateWithLifecycle()

    val themeManager: ThemeManager = koinInject()
    val themeMode by themeManager.themeMode.collectAsState()
    val systemDark = isSystemInDarkTheme()

    val isDarkTheme = when (themeMode) {
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
        ThemeMode.SYSTEM -> systemDark
    }

    ProfileContent(
        isDarkMode = isDarkTheme,
        isLoginUser = isLoginUser,
        user = profile,
        onClickBack = { navHostController.popBackStack() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(
    isDarkMode: Boolean,
    isLoginUser: Boolean,
    user: UserUIData?,
    onClickBack: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    val title = if (isLoginUser) "Profil Saya" else "Profil Pengguna"
                    Text(text = title, style = TalangragaTypography.titleLarge)
                },
                modifier = Modifier,
//                navigationIcon = {
//                    IconButton(
//                        onClick = onClickBack,
//                    ) {
//                        Icon(
//                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                            contentDescription = null,
//                            modifier = Modifier
//                        )
//                    }
//
//                },
                actions = {
                    TextButton(onClick = { }) {
                        Text(
                            text = "Edit",
                            style = TalangragaTypography.bodyMedium.copy(color = MaterialTheme.colorScheme.tertiary)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            val (imageProfileRef, fullNameRef, usernameRef, cardInfoRef, themeRef) = createRefs()

            BasicImage(
                model = user?.imageProfileUrl.orEmpty(),
                modifier = Modifier
                    .size(124.dp)
                    .clip(CircleShape)
                    .constrainAs(imageProfileRef) {
                        top.linkTo(parent.top, 8.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )

            Icon(
                imageVector = Icons.Filled.CameraAlt,
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .border(width = 1.dp, color = Color.White, shape = CircleShape)
                    .background(color = Color.White)
                    .padding(2.dp)
                    .border(width = 1.dp, color = Color.DarkGray, shape = CircleShape)
                    .padding(4.dp)
                    .constrainAs(createRef()) {
                        end.linkTo(imageProfileRef.end, 4.dp)
                        bottom.linkTo(imageProfileRef.bottom, 4.dp)
                    }
            )

            Text(
                text = user?.fullname.orEmpty(),
                style = TalangragaTypography.titleLarge,
                modifier = Modifier.constrainAs(fullNameRef) {
                    top.linkTo(imageProfileRef.bottom, 8.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )

            val username = "@${user?.username.orEmpty()}"
            Text(
                text = username,
                modifier = Modifier.constrainAs(usernameRef) {
                    top.linkTo(fullNameRef.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )

            Card(
                modifier = Modifier.constrainAs(cardInfoRef) {
                    top.linkTo(usernameRef.bottom, 16.dp)
                    start.linkTo(parent.start, 16.dp)
                    end.linkTo(parent.end, 16.dp)
                    width = Dimension.fillToConstraints
                },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    UserInfoItem(icon = Icons.Filled.Phone, text = user?.phone.orEmpty())
                    UserInfoItem(icon = Icons.Filled.Email, text = user?.email.orEmpty())
                    UserInfoItem(icon = Icons.Filled.Place, text = user?.domicile.orEmpty())
                }
            }

            if (isLoginUser) {
                Card(
                    modifier = Modifier.padding(16.dp).constrainAs(themeRef) {
                        top.linkTo(cardInfoRef.bottom, 8.dp)
                        start.linkTo(parent.start, 16.dp)
                        end.linkTo(parent.end, 16.dp)
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    ConstraintLayout(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                        val (iconRef, modeRef, switchRef) = createRefs()
                        IconBlock(
                            icon = Icons.Filled.LightMode,
                            startColor = Sage,
                            endColor = Sage,
                            size = 40.dp,
                            iconSize = 24.dp,
                            modifier = Modifier.constrainAs(iconRef) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                            }
                        )
                        val mode = if (isDarkMode) "Gelap" else "Terang"
                        Text(
                            text = "Mode $mode",
                            style = TalangragaTypography.titleLarge.copy(
                                fontWeight = FontWeight.Medium,
                                fontSize = 20.sp
                            ),
                            modifier = Modifier.constrainAs(modeRef) {
                                top.linkTo(iconRef.top)
                                bottom.linkTo(iconRef.bottom)
                                start.linkTo(iconRef.end, 16.dp)
                            }
                        )

                        ThemeToggleScreen(Modifier.fillMaxWidth().constrainAs(switchRef) {
                            start.linkTo(iconRef.start)
                            top.linkTo(iconRef.bottom, 16.dp)
                        })

//                        Switch(
//                            checked = isDarkMode,
//                            onCheckedChange = onDarkModeChange,
//                            thumbContent = {
//                                Icon(
//                                    imageVector = if (isDarkMode) Icons.Filled.DarkMode else Icons.Filled.LightMode,
//                                    contentDescription = null,
//                                    modifier = Modifier
//                                        .size(SwitchDefaults.IconSize)
//                                )
//                            },
//                            modifier = Modifier.constrainAs(switchRef) {
//                                end.linkTo(parent.end)
//                                top.linkTo(parent.top)
//                                bottom.linkTo(parent.bottom)
//                            }
//                        )
                    }
                }
            }
        }
    }
}

@Composable
fun UserInfoItem(icon: ImageVector, text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        IconBlock(
            icon = icon,
            startColor = Sage,
            endColor = Sage,
            size = 40.dp,
            iconSize = 24.dp,
            modifier = Modifier
        )
        Text(
            text = text,
            style = TalangragaTypography.titleLarge.copy(
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp
            ),
            modifier = Modifier
        )
    }
}

@Composable
fun ThemeToggleScreen(modifier: Modifier, themeManager: ThemeManager = koinInject()) {
    val themeMode by themeManager.themeMode.collectAsState()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Select Theme:")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ThemeMode.entries.forEach { mode ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.clickable { themeManager.setTheme(mode) }
                ) {
                    RadioButton(
                        selected = themeMode == mode,
                        onClick = { themeManager.setTheme(mode) })
                    Text(text = mode.name)
                }
            }
        }

    }
}


@Composable
@Preview(showBackground = true)
fun PreviewProfileContent() {
    TalangragaTheme(useDynamicColor = false) {
        ProfileContent(
            user = UserUIData(
                1, "iqbalfauzi", "Iqbal Fauzi", "work.iqbalfauzi@gmail.com", "087822882668",
                domicile = "Bandung",
                userType = "admin",
                imageProfileUrl = "",
                isActive = true
            ),
            onClickBack = { },
            isLoginUser = true,
            isDarkMode = false,
        )
    }
}
