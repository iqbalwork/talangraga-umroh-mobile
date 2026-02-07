package com.talangraga.umrohmobile.presentation.user

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.talangraga.umrohmobile.navigation.Screen
import com.talangraga.umrohmobile.presentation.user.model.UserUIData
import com.talangraga.umrohmobile.ui.component.BasicImage
import com.talangraga.umrohmobile.ui.component.InputText
import com.talangraga.umrohmobile.ui.component.TalangragaScaffold
import com.talangraga.umrohmobile.ui.component.ToastManager
import com.talangraga.umrohmobile.ui.component.ToastType
import com.talangraga.umrohmobile.ui.theme.TalangragaTheme
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import talangragaumrohmobile.composeapp.generated.resources.Res
import talangragaumrohmobile.composeapp.generated.resources.search_username

@Composable
fun ListUserScreen(
    rootNavController: NavHostController,
    navHostController: NavHostController,
    viewModel: ListUserViewModel = koinViewModel(),
) {

    val state = viewModel.uiState.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()
    
    LaunchedEffect(errorMessage) {
        if (!errorMessage.isNullOrEmpty()) {
            ToastManager.show(message = errorMessage.orEmpty(), type = ToastType.Error)
            viewModel.clearError()
        }
    }

    ListUserContent(
        state = state.value,
        searchQuery = searchQuery,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onBackClick = {
            navHostController.popBackStack()
        },
        onUserClick = {
            navHostController.navigate(Screen.MemberDetailRoute(it.id))
        },
        onAddUserClick = {
            navHostController.navigate(
                Screen.AddUserRoute(
                    userId = 0,
                    isEdit = false,
                    isLoginUser = false
                )
            )
        },
        onEditUser = {
            navHostController.navigate(
                Screen.AddUserRoute(
                    userId = it,
                    isEdit = true,
                    isLoginUser = false
                )
            )
//            navHostController.navigate(Screen.EditProfileRoute(userId = it, isLoginUser = false))
        },
        onRefresh = viewModel::getListUser
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListUserContent(
    onBackClick: (() -> Unit)? = null,
    onAddUserClick: (() -> Unit),
    onEditUser: (Int) -> Unit,
    onUserClick: (UserUIData) -> Unit,
    state: ListUserUiState,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onRefresh: () -> Unit
) {

    val localDensity = LocalDensity.current
    var buttonHeight by remember { mutableStateOf(0.dp) }

    val refreshState = rememberPullToRefreshState()

    TalangragaScaffold(
        contentWindowInsets = WindowInsets.statusBars,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Daftar Anggota", style = MaterialTheme.typography.titleLarge) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onAddUserClick()
                },
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .onGloballyPositioned { coordinates ->
                        buttonHeight = with(localDensity) { coordinates.size.height.toDp() }
                    }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Transaction",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { paddingValues ->

        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                InputText(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    placeholder = stringResource(Res.string.search_username),
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .padding(horizontal = 16.dp)
                )

                PullToRefreshBox(
                    state = refreshState,
                    isRefreshing = state is ListUserUiState.Loading && searchQuery.isEmpty(), // Only show refresh indicator when loading full list
                    onRefresh = onRefresh,
                    modifier = Modifier.weight(1f) // Fill remaining space for list
                ) {
                    when (state) {
                        ListUserUiState.EmptyData -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Tidak ada data.")
                            }
                        }

                        ListUserUiState.Loading -> {
                            // Show loading indicator only if not refreshing (initial load)
                            // PullToRefreshBox handles the spinner for refresh
                            if (refreshState.isAnimating) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            } else {
                                // Empty content while refreshing if you prefer, or keep showing list
                                Box(modifier = Modifier.fillMaxSize())
                            }
                        }

                        is ListUserUiState.Success -> {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                itemsIndexed(state.users) { index, user ->
                                    UserItem(
                                        user = user,
                                        onEditUser = onEditUser,
                                        modifier = Modifier.padding(bottom = if (index == state.users.lastIndex) buttonHeight + 16.dp else 0.dp)
                                            .clickable {
                                                onUserClick(user)
                                            }
                                            .fillMaxWidth()
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UserItem(
    user: UserUIData,
    onEditUser: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // User Avatar
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                if (user.imageProfileUrl.isNotBlank()) {
                    BasicImage(
                        model = user.imageProfileUrl,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "User Avatar",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // User Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = user.fullname,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Role Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            if (user.userType == "admin")
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                            else
                                MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = user.userType.replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Medium,
                        color = if (user.userType == "admin")
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.secondary,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(4.dp))

            IconButton(
                modifier = Modifier,
                onClick = { onEditUser(user.id) }
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ListUserContentSuccessPreview() {
    val users = listOf(
        UserUIData(
            id = 1,
            fullname = "John Doe",
            phone = "081234567890",
            userType = "Admin",
            username = "",
            email = "johndoe@mail.com",
            domicile = "Bandung",
            imageProfileUrl = "",
            isActive = true
        ),
        UserUIData(
            id = 1,
            fullname = "John Doe",
            phone = "081234567890",
            userType = "Member",
            username = "",
            email = "johndoe@mail.com",
            domicile = "Bandung",
            imageProfileUrl = "",
            isActive = true
        )
    )
    TalangragaTheme(useDynamicColor = false) {
        ListUserContent(
            onUserClick = {},
            state = ListUserUiState.Success(users),
            onBackClick = { },
            onAddUserClick = { },
            onEditUser = {},
            searchQuery = "",
            onSearchQueryChange = {},
            onRefresh = {}
        )
    }
}
