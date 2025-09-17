package com.talangraga.umrohmobile.presentation.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.talangraga.umrohmobile.data.local.database.model.PeriodEntity
import com.talangraga.umrohmobile.data.local.database.model.UserEntity
import com.talangraga.umrohmobile.presentation.navigation.HomeRoute
import com.talangraga.umrohmobile.presentation.navigation.LoginRoute
import com.talangraga.umrohmobile.ui.Aqua
import com.talangraga.umrohmobile.ui.Background
import com.talangraga.umrohmobile.ui.Green
import com.talangraga.umrohmobile.ui.InterFont
import com.talangraga.umrohmobile.ui.MediumPurple
import com.talangraga.umrohmobile.ui.RosePink
import com.talangraga.umrohmobile.ui.TalangragaTheme
import com.talangraga.umrohmobile.ui.UmrohMobileTypography
import com.talangraga.umrohmobile.ui.section.CardInfoSection
import com.talangraga.umrohmobile.ui.section.DialogPeriods
import com.talangraga.umrohmobile.ui.section.DialogUserType
import com.talangraga.umrohmobile.util.currentDate
import com.talangraga.umrohmobile.util.formatDateRange
import com.talangraga.umrohmobile.util.isDateInRange
import io.github.aakira.napier.Napier
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
    val periods by viewModel.periods.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()

    if (justLogin) {
        viewModel.getProfile()
    } else {
        viewModel.getLocalProfile()
    }

    HomeContent(
        user = profile,
        periods = periods
    ) {
        viewModel.clearSession()
        navHostController.navigate(LoginRoute) {
            popUpTo(HomeRoute()) {
                inclusive = true
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    user: UserEntity?,
    periods: List<PeriodEntity>,
    onLogout: () -> Unit
) {

    val fontFamily = InterFont()
    val context = LocalPlatformContext.current
    var userType by remember { mutableStateOf(user?.userType) }
    val userTypeIcon by remember {
        derivedStateOf {
            if (userType == "Admin") Icons.Default.Security else Icons.Default.AccountCircle
        }
    }
    var period by remember { mutableStateOf(periods.firstOrNull()) }
    val userTypeSheetState = rememberModalBottomSheetState()
    val userTypeScope = rememberCoroutineScope()
    var userTypeShowBottomSheet by remember { mutableStateOf(false) }
    val periodSheetState = rememberModalBottomSheetState()
    val periodScope = rememberCoroutineScope()
    var periodShowBottomSheet by remember { mutableStateOf(false) }

    LaunchedEffect(periods) {
        Napier.i(message = "Current Date : $currentDate")
        if (periods.isNotEmpty()) {
            for (data in periods) {
                Napier.i(message = data.periodeName)
                if (currentDate.isDateInRange(data.startDate, data.endDate)) {
                    period = data
                    Napier.i(message = period?.periodeName.orEmpty())
                    break
                }
            }
        }
    }

    LaunchedEffect(user) {
        user?.let {
            userType = it.userType
        }
    }

    Scaffold { paddingValues ->

        if (userTypeShowBottomSheet) {
            DialogUserType(
                modifier = Modifier,
                sheetState = userTypeSheetState,
                scope = userTypeScope,
                onBottomSheetChange = {
                    userTypeShowBottomSheet = it
                },
                onChooseUserType = {
                    userType = it
                }
            )
        }

        if (periodShowBottomSheet) {
            DialogPeriods(
                modifier = Modifier,
                sheetState = periodSheetState,
                scope = periodScope,
                periods = periods,
                onBottomSheetChange = {
                    periodShowBottomSheet = it
                },
                onChoosePeriod = {
                    period = it
                }
            )
        }

        Column(
            modifier = Modifier
                .background(color = Background)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .background(color = Color.White)
                    .padding(top = paddingValues.calculateTopPadding())
                    .fillMaxWidth()
            ) {
                val (imageProfileRef, nameRef, userTypeRef) = createRefs()

                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(user?.imageProfileUrl)
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
                        }
                )

                Text(
                    text = user?.fullname.orEmpty(),
                    style = UmrohMobileTypography(fontFamily).basicTextStyle.copy(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
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
                        .padding(bottom = 16.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .clickable {
                            userTypeShowBottomSheet = true
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
                    Icon(
                        imageVector = userTypeIcon,
                        contentDescription = "Admin",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = userType.orEmpty(),
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                    val rotate by animateFloatAsState(if (userTypeShowBottomSheet) 180f else 0f)
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Expand",
                        tint = Color.White,
                        modifier = Modifier
                            .size(16.dp)
                            .rotate(rotate)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .align(Alignment.Start)
                    .background(color = Color.White, shape = RoundedCornerShape(12.dp))
                    .border(1.dp, color = Color.Gray, shape = RoundedCornerShape(12.dp))
                    .clip(RoundedCornerShape(12.dp))
                    .clickable(onClick = {
                        periodShowBottomSheet = true
                    })
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(imageVector = Icons.Filled.EditCalendar, contentDescription = null)
                val dateLabel = if (period?.periodeName.isNullOrBlank()) {
                    "-"
                } else {
                    "${period?.periodeName.orEmpty()} : ${
                        formatDateRange(
                            period?.startDate.orEmpty(),
                            period?.endDate.orEmpty()
                        )
                    }"
                }
                Text(
                    text = dateLabel,
                    style = UmrohMobileTypography(fontFamily).basicTextStyle.copy(),
                    modifier = Modifier
                )
            }
            CardInfoSection(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                title = "Total Tabungan Periode ini",
                value = "Rp 72.000.000",
                notes = "12% dari bulan lalu",
                notesColor = Green,
                icon = Icons.Default.AttachMoney,
                startIconColor = Aqua,
                endIconColor = Green
            )
            CardInfoSection(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                title = "Anggota yang Menabung",
                value = "142",
                notes = "Bulan ini",
                notesColor = MediumPurple,
                icon = Icons.Default.AccountCircle,
                startIconColor = MediumPurple,
                endIconColor = MediumPurple
            )
            CardInfoSection(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                title = "Rata-rata Tabungan",
                value = "Rp 455,.696",
                notes = "Per Anggota/Bulan",
                notesColor = RosePink,
                icon = Icons.Default.Calculate,
                startIconColor = RosePink,
                endIconColor = RosePink
            )
        }
    }
}

@Preview
@Composable
fun HomeContentPreview() {
    TalangragaTheme {
        HomeContent(
            UserEntity(
                userId = 1,
                userName = "iqbalf",
                fullname = "Iqbal Fauzi",
                email = "",
                phone = "",
                domisili = "",
                userType = "Admin",
                imageProfileUrl = ""
            ),
            periods = listOf(
                PeriodEntity("", "Bulan ke 1", "2025-08-06", "2025-09-05"),
            )
        ) {

        }
    }
}