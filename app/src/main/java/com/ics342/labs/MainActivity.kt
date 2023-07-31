package com.ics342.labs

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.ics342.labs.ui.theme.LabsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var hasPermission by remember { mutableStateOf(false) }
            val context = LocalContext.current
            val launcher = rememberLauncherForActivityResult(RequestPermission()) {
                if (it) {
                    startNotificationService(context)
                }
            }
            LabsTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Button(
                            onClick = {
                                checkOrRequestPermission(
                                    context,
                                    launcher
                                ) { startNotificationService(context) }
                            }
                        ) {
                            Text("Show Notification")
                        }
                    }
                }
            }
        }
    }
}

fun startNotificationService(context: Context) {
    // ("Start the Notification Service")
    val intent = Intent(context, NotificationService::class.java)
    context.startService(intent)
}

private fun checkOrRequestPermission(
    context: Context,
    launcher: ManagedActivityResultLauncher<String, Boolean>,
    permissionGranted: () -> Unit
) {
    if (
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED) {
        permissionGranted()
    } else {
        launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }
}
