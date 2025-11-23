package com.talangraga.umrohmobile

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.talangraga.umrohmobile.application.App

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted. You can now send notifications.
                // For example, you might call a function to send a test notification.
                // Or you might save a flag in SharedPreferences.
            } else {
                // Permission is denied. You should tell the user why notifications are important.
                // A good practice is to show a dialog explaining the feature's importance.
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
        requestNotificationPermission()
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                // Check if the permission is already granted
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // Permission is already granted. You can proceed.
                }

                // Check if you should show an educational UI
                shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                    // Show a dialog or an in-app message explaining why notifications are needed.
                    // For example, "This app needs notification access to send you important updates."
                    // Then, after the user acknowledges, launch the permission request.
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }

                // Request the permission for the first time
                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
