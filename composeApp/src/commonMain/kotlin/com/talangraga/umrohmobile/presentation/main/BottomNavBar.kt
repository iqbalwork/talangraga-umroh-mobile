package com.talangraga.umrohmobile.presentation.main

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.talangraga.shared.Sage
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
            label = { Text("Beranda") },
            colors = NavigationBarItemDefaults.colors(selectedTextColor = Sage)
        )
        NavigationBarItem(
            selected = selected is BottomNavRoute.Transaction,
            onClick = { onSelect(BottomNavRoute.Transaction) },
            icon = { Icon(Icons.Default.History, null) },
            label = { Text("Transaksi") },
            colors = NavigationBarItemDefaults.colors(selectedTextColor = Sage)
        )
        NavigationBarItem(
            selected = selected is BottomNavRoute.Member,
            onClick = { onSelect(BottomNavRoute.Member) },
            icon = { Icon(Icons.Default.Group, null) },
            label = { Text("Anggota") },
            colors = NavigationBarItemDefaults.colors(selectedTextColor = Sage)
        )
        NavigationBarItem(
            selected = selected is BottomNavRoute.Profile,
            onClick = { onSelect(BottomNavRoute.Profile) },
            icon = { Icon(Icons.Default.Person, null) },
            label = { Text("Akun") },
            colors = NavigationBarItemDefaults.colors(selectedTextColor = Sage)
        )
    }
}
