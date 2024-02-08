package com.mathias8dev.notificationmanager

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mathias8dev.notificationmanager.domain.notification.NotificationDataRaw
import com.mathias8dev.notificationmanager.domain.notification.NotificationUtils
import com.mathias8dev.notificationmanager.ui.theme.NotificationManagerTheme
import com.mathias8dev.permissionhelper.permission.Permission
import com.mathias8dev.permissionhelper.permission.PermissionHelper
import com.mathias8dev.permissionhelper.permission.isGranted

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NotificationUtils.initDefaultChannel(this)

        setContent {
            NotificationManagerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainContent(
                        onNotifyClicked = {
                            NotificationUtils.notify(
                                this,
                                channel = NotificationUtils.Channel.Default,
                                notificationData = NotificationDataRaw(
                                    id = 1,
                                    iconRes = R.drawable.ic_launcher_background,
                                    title = "Notification",
                                    content = "Hello",
                                ),
                                onAddMoreCustomization = {
                                    it.setAutoCancel(false)
                                }
                            )
                        }
                    )
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MainContent(
    onNotifyClicked: () -> Unit
) {

    val localContext = LocalContext.current

    val notificationPermission = remember {
        Permission(
            manifestKey = Manifest.permission.POST_NOTIFICATIONS
        )
    }



    PermissionHelper(
        permission = notificationPermission
    ) {
        val permissionState = getPermissionState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            TextButton(
                onClick = {
                    launchPermission {
                        Toast.makeText(
                            localContext,
                            if (it.isGranted) "Notifications permissions enable successfully"
                            else "The notification permission is not enable",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }) {
                Text("Request notification permission")
            }
            TextButton(
                onClick = {
                    if (!permissionState.isGranted) {
                        launchPermission {  }
                    }else {
                        onNotifyClicked()
                    }
                }
            ) {
                Text("Notify")
            }
        }

    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NotificationManagerTheme {
        Greeting("Android")
    }
}