package com.talangraga.umrohmobile.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun HomeScreen(onLogoutClicked: () -> Unit, modifier: Modifier = Modifier) {
    Scaffold {
        Column(modifier = Modifier.padding(it)) {
            Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Logout")
            }
        }
    }
}