package com.talangraga.umrohmobile.presentation.main

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.talangraga.data.local.session.Session
import com.talangraga.data.local.session.SessionKey
import com.talangraga.shared.TalangragaTypography
import com.talangraga.umrohmobile.navigation.BottomNavRoute
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import talangragaumrohmobile.composeapp.generated.resources.Res
import talangragaumrohmobile.composeapp.generated.resources.talangraga_logo

private data class NavItemData(
    val route: BottomNavRoute,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

/**
 * Modern Telegram-style Floating Pill Bottom Navigation Bar.
 * Floats gracefully over content with rounded capsule container and animated pill indicator.
 */
@Composable
fun FloatingBottomNavBar(
    selected: BottomNavRoute,
    onSelect: (BottomNavRoute) -> Unit,
    modifier: Modifier = Modifier
) {
    val session: Session = koinInject()
    val isLogin = session.getBoolean(SessionKey.IS_LOGGED_IN)
    val userType = session.userProfile.value?.userType.orEmpty()

    val navItems = buildList {
        add(
            NavItemData(
                route = BottomNavRoute.Home,
                label = "Beranda",
                selectedIcon = Icons.Filled.Home,
                unselectedIcon = Icons.Outlined.Home
            )
        )
        add(
            NavItemData(
                route = BottomNavRoute.Transaction,
                label = "Tabungan",
                selectedIcon = Icons.Filled.History,
                unselectedIcon = Icons.Outlined.History
            )
        )
        if (isLogin && userType.lowercase() == "admin") {
            add(
                NavItemData(
                    route = BottomNavRoute.Periode,
                    label = "Periode",
                    selectedIcon = Icons.Filled.DateRange,
                    unselectedIcon = Icons.Outlined.DateRange
                )
            )
            add(
                NavItemData(
                    route = BottomNavRoute.Member,
                    label = "Anggota",
                    selectedIcon = Icons.Filled.Group,
                    unselectedIcon = Icons.Outlined.Group
                )
            )
        }
        add(
            NavItemData(
                route = BottomNavRoute.Profile,
                label = "Akun",
                selectedIcon = Icons.Filled.Person,
                unselectedIcon = Icons.Outlined.Person
            )
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .widthIn(max = 480.dp)
                .fillMaxWidth()
                .shadow(
                    elevation = 10.dp,
                    shape = RoundedCornerShape(32.dp),
                    spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.25f),
                    ambientColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                ),
            shape = RoundedCornerShape(32.dp),
            color = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.95f),
            tonalElevation = 6.dp,
            border = BorderStroke(
                1.dp,
                MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                navItems.forEach { item ->
                    val isSelected = selected == item.route

                    FloatingNavItem(
                        item = item,
                        isSelected = isSelected,
                        onClick = { onSelect(item.route) }
                    )
                }
            }
        }
    }
}

@Composable
private fun FloatingNavItem(
    item: NavItemData,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val animatedPillWidth by animateDpAsState(
        targetValue = if (isSelected) 56.dp else 40.dp,
        animationSpec = spring(dampingRatio = 0.8f, stiffness = 400f)
    )
    val containerColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primaryContainer else androidx.compose.ui.graphics.Color.Transparent,
        animationSpec = tween(200)
    )
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
        animationSpec = tween(200)
    )
    val textColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
        animationSpec = tween(200)
    )

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true, radius = 28.dp),
                onClick = onClick
            )
            .padding(vertical = 4.dp, horizontal = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Pill capsule around icon
        Box(
            modifier = Modifier
                .height(30.dp)
                .width(animatedPillWidth)
                .clip(CircleShape)
                .background(containerColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                contentDescription = item.label,
                tint = contentColor,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = item.label,
            style = TalangragaTypography.bodySmall.copy(
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            ),
            color = textColor,
            maxLines = 1
        )
    }
}

/**
 * Adaptive Navigation Rail for Tablet / Foldable / Landscape screens.
 */
@Composable
fun AdaptiveNavRail(
    selected: BottomNavRoute,
    onSelect: (BottomNavRoute) -> Unit,
    modifier: Modifier = Modifier
) {
    val session: Session = koinInject()
    val isLogin = session.getBoolean(SessionKey.IS_LOGGED_IN)
    val userType = session.userProfile.value?.userType.orEmpty()

    val navItems = buildList {
        add(
            NavItemData(
                route = BottomNavRoute.Home,
                label = "Beranda",
                selectedIcon = Icons.Filled.Home,
                unselectedIcon = Icons.Outlined.Home
            )
        )
        add(
            NavItemData(
                route = BottomNavRoute.Transaction,
                label = "Tabungan",
                selectedIcon = Icons.Filled.History,
                unselectedIcon = Icons.Outlined.History
            )
        )
        if (isLogin && userType.lowercase() == "admin") {
            add(
                NavItemData(
                    route = BottomNavRoute.Periode,
                    label = "Periode",
                    selectedIcon = Icons.Filled.DateRange,
                    unselectedIcon = Icons.Outlined.DateRange
                )
            )
            add(
                NavItemData(
                    route = BottomNavRoute.Member,
                    label = "Anggota",
                    selectedIcon = Icons.Filled.Group,
                    unselectedIcon = Icons.Outlined.Group
                )
            )
        }
        add(
            NavItemData(
                route = BottomNavRoute.Profile,
                label = "Akun",
                selectedIcon = Icons.Filled.Person,
                unselectedIcon = Icons.Outlined.Person
            )
        )
    }

    NavigationRail(
        modifier = modifier.fillMaxHeight(),
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        header = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                Image(
                    painter = painterResource(Res.drawable.talangraga_logo),
                    contentDescription = "Logo",
                    modifier = Modifier.size(44.dp)
                )
            }
        }
    ) {
        Spacer(Modifier.height(16.dp))
        navItems.forEach { item ->
            val isSelected = selected == item.route

            NavigationRailItem(
                selected = isSelected,
                onClick = { onSelect(item.route) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.label
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        style = TalangragaTypography.bodySmall.copy(fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium)
                    )
                },
                colors = NavigationRailItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}
