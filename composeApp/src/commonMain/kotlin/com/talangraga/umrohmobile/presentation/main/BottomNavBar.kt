package com.talangraga.umrohmobile.presentation.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.talangraga.umrohmobile.presentation.navigation.BottomNavRoute

@Composable
fun BottomNavBar(
    selected: BottomNavRoute,
    onSelect: (BottomNavRoute) -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = selected is BottomNavRoute.Home,
            onClick = { onSelect(BottomNavRoute.Home) },
            icon = { Icon(Icons.Default.Home, null) },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = selected is BottomNavRoute.Member,
            onClick = { onSelect(BottomNavRoute.Member) },
            icon = { Icon(Icons.Default.Group, null) },
            label = { Text("Member") }
        )
        NavigationBarItem(
            selected = selected is BottomNavRoute.Profile,
            onClick = { onSelect(BottomNavRoute.Profile) },
            icon = { Icon(Icons.Default.Person, null) },
            label = { Text("Profile") }
        )
    }
}
