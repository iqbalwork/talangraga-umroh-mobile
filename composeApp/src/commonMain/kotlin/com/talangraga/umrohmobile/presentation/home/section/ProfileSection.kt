package com.talangraga.umrohmobile.presentation.home.section

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.talangraga.shared.TalangragaTypography
import com.talangraga.umrohmobile.presentation.user.model.UserUIData
import com.talangraga.umrohmobile.ui.component.BasicImage
import com.talangraga.umrohmobile.ui.theme.TalangragaTheme

@Composable
fun ProfileSection(
    modifier: Modifier = Modifier,
    user: UserUIData?,
    userType: String?,
    onClickImage: (String) -> Unit = {},
    onRetry: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // User Avatar
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            if (!user?.imageProfileUrl.isNullOrBlank()) {
                BasicImage(
                    model = user?.imageProfileUrl,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CircleShape)
                        .clickable { onClickImage(user?.imageProfileUrl.orEmpty()) }
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Avatar",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = user?.fullname ?: "Pengguna",
                style = TalangragaTypography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (userType?.lowercase() == "admin")
                        MaterialTheme.colorScheme.primaryContainer
                    else
                        MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Text(
                    text = userType?.replaceFirstChar { it.titlecase() } ?: "Member",
                    style = TalangragaTypography.bodySmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.sp
                    ),
                    color = if (userType?.lowercase() == "admin")
                        MaterialTheme.colorScheme.onPrimaryContainer
                    else
                        MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewProfileSection() {
    val dummyUser = UserUIData(
        id = "1",
        fullname = "Iqbal Fauzi",
        email = "work.iqbalfauzi@gmail.com",
        phone = "087822882668",
        imageProfileUrl = "",
        userType = "Admin",
        username = "iqbalf",
        domicile = "Bandung",
        isActive = true,
    )
    TalangragaTheme {
        ProfileSection(
            userType = "Admin",
            user = dummyUser
        )
    }
}
