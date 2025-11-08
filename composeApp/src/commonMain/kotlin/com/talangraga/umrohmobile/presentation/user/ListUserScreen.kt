package com.talangraga.umrohmobile.presentation.user

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.talangraga.umrohmobile.AppViewModel
import com.talangraga.umrohmobile.data.local.database.model.UserEntity
import com.talangraga.umrohmobile.ui.TalangragaTheme
import com.talangraga.umrohmobile.ui.component.InputText
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import talangragaumrohmobile.composeapp.generated.resources.Res
import talangragaumrohmobile.composeapp.generated.resources.search_username

data class User(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val role: String,
    val imageUrl: String? = null
)

@Composable
fun ListUserScreen(
    navHostController: NavHostController,
    viewModel: ListUserViewModel = koinViewModel(),
) {

    val state = viewModel.uiState.collectAsStateWithLifecycle()

    ListUserContent(
        state = state.value,
        onBackClick = {
            navHostController.popBackStack()
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListUserContent(
    onBackClick: (() -> Unit)? = null,
    onAddUserClick: (() -> Unit)? = null,
    state: ListUserUiState
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Daftar Anggota", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = { onBackClick?.invoke() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAddUserClick?.invoke() },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add User",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { innerPadding ->

        var username by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            InputText(
                value = username,
                onValueChange = {
                    username = it
                },
                placeholder = stringResource(Res.string.search_username),
                backgroundColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .padding(horizontal = 16.dp)
            )

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
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is ListUserUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier,
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        itemsIndexed(state.users) { index, user ->
                            UserItem(
                                user = User(
                                    id = user.userId.toString(),
                                    name = user.fullname,
                                    email = user.email,
                                    phone = user.phone,
                                    role = user.userType
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UserItem(
    user: User,
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
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "User Avatar",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // User Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = user.phone,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            // Role Badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        if (user.role == "Admin")
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        else
                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = user.role,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Medium,
                    color = if (user.role == "Admin")
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.secondary,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewListUserContent() {
    TalangragaTheme(useDynamicColor = false) {
        ListUserContent(
            onBackClick = {},
            onAddUserClick = {},
            state = ListUserUiState.Success(
                listOf(
                    UserEntity(
                        userId = 1,
                        userName = "johndoe",
                        fullname = "John Doe",
                        email = "john@mail.com",
                        phone = "+62",
                        domisili = "Bandung",
                        userType = "Admin",
                        imageProfileUrl = ""
                    ),
                    UserEntity(
                        userId = 1,
                        userName = "doejohn",
                        fullname = "Doe John",
                        email = "doe@mail.com",
                        phone = "+621",
                        domisili = "Bandung",
                        userType = "Member",
                        imageProfileUrl = ""
                    )
                )
            ),
        )
    }
}

@Preview
@Composable
fun PreviewUserItem() {
    UserItem(
        user = User(
            id = "1",
            name = "John Doe",
            email = "john.doe@example.com",
            phone = "+62 812 3456 7890",
            role = "Admin"
        )
    )
}

@Preview
@Composable
fun PreviewUserItemRegularUser() {
    UserItem(
        user = User(
            id = "2",
            name = "Jane Smith",
            email = "jane.smith@example.com",
            phone = "+62 813 9876 5432",
            role = "User"
        )
    )
}
