package com.example.cineflix.Screen.settingscreen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DataUsage
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import androidx.core.net.toUri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSettingsScreen(navController: NavHostController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val notificationToggle by SettingsDataStore.getNotificationSetting(context).collectAsState(initial = false)
    val wifiOnlyToggle by SettingsDataStore.getWiFiOnlySetting(context).collectAsState(initial = false)
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "App Settings",
                        color = Color.White,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        },
        containerColor = Color.Black
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .background(Color.Black)
                .padding(bottom = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            SettingsSection(title = "Video Playback") {
                SettingsItem(
                    title = "Mobile Data Usage",
                    subtitle = "Automatic",
                    icon = Icons.Default.DataUsage
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            SettingsSection(title = "Notifications") {
                SettingsItemWithSwitch(
                    title = "Allow notifications",
                    subtitle = "Customise in Settings → Notifications",
                    icon = Icons.Default.Notifications,
                    checked = notificationToggle,
                    onCheckedChange = {
                        scope.launch {
                            SettingsDataStore.saveNotificationSetting(context, it)
                        }
                    },
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            SettingsSection(title = "Downloads") {
                SettingsItemWithSwitch(
                    title = "Wi-Fi Only", icon = Icons.Default.Wifi,
                    checked = wifiOnlyToggle,
                    onCheckedChange = {
                        scope.launch {
                            SettingsDataStore.saveWiFiOnlySetting(context, it)
                        }
                    },
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(Color.Black)
                )

                SettingsItem(
                    title = "Download Video Quality",
                    subtitle = "Standard",
                    icon = Icons.Default.Download
                )

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(Color.Black)
                )

                SettingsItem(
                    title = "Download Location",
                    subtitle = "Internal Storage",
                    icon = Icons.Default.Storage
                )

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(Color.Black)
                )

                StorageBar(
                    used = 65f,
                    netflix = 0.000018f,
                    free = 52f
                )
            }
            fun openUrl(url: String) {
                val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                context.startActivity(intent)
            }

            Spacer(modifier = Modifier.height(20.dp))

            SettingsRow(
                icon = Icons.Default.Person,
                title = "Account",
                subtitle = "Email: shrikants0421@gmail.com",
                showExternalIcon = true
            )

            Spacer(modifier = Modifier.height(20.dp))

            SectionHeader("Diagnostics")
            SettingsRow(icon = Icons.Default.Wifi, title = "Check network")
            SettingsRow(icon = Icons.Default.PlayCircleFilled, title = "Playback Specification")
            SettingsRow(
                icon = Icons.Default.Speed,
                title = "Internet speed test",
                showExternalIcon = true,
                url = "https://fast.com/",
                onExternalClick = { openUrl(it) }
            )
            Spacer(modifier = Modifier.height(20.dp))
            SectionHeader("Legal")
            SettingsRow(icon = Icons.Default.Description, title = "Open Source Licences")
            SettingsRow(icon = Icons.Default.Description, title = "Privacy", showExternalIcon = true,url = "https://help.netflix.com/en/node/100628",
                onExternalClick = { openUrl(it) })
            SettingsRow(icon = Icons.Default.Description, title = "Cookie Preferences", showExternalIcon = true,url = "https://help.netflix.com/en/node/124516",
                onExternalClick = { openUrl(it) })
            SettingsRow(
                icon = Icons.Default.Description,
                title = "Terms of Use",
                showExternalIcon = true,
                url = "https://brand.netflix.com/en/terms/",
                onExternalClick = { openUrl(it) }
            )
        }
    }
}



@Composable
fun SettingsSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column {
        Text(
            text = title,
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF19191A), shape = RoundedCornerShape(4.dp))
                .padding(start = 20.dp)
        ) {
            content()
        }
    }
}

@Composable
fun SettingsRow(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    showExternalIcon: Boolean = false,
    url: String? = null,
    onExternalClick: ((String) -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1C1C1E))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = title, tint = Color.White, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, color = Color.White, fontSize = 15.sp)
                subtitle?.let {
                    Text(text = it, color = Color.Gray, fontSize = 13.sp)
                }
            }
        }
        if (showExternalIcon && url != null && onExternalClick != null) {
            Icon(
                imageVector = Icons.Default.OpenInNew,
                contentDescription = "Open",
                tint = Color.White,
                modifier = Modifier.clickable{
                    onExternalClick(url)
                }
            )
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        color = Color.White,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    )
}

@Composable
fun SettingsItem(
    title: String,
    subtitle: String = "",
    icon: ImageVector
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = Color.White,
            modifier = Modifier.padding(end = 16.dp)
        )
        Column {
            Text(text = title, color = Color.White, fontSize = 19.sp)
            if (subtitle.isNotEmpty()) {
                Text(text = subtitle, color = Color.Gray, fontSize = 15.sp)
            }
        }
    }
}

@Composable
fun SettingsItemWithSwitch(
    title: String,
    subtitle: String = "",
    icon: ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color.White,
                modifier = Modifier.padding(end = 16.dp)
            )
            Column {
                Text(text = title, color = Color.White, fontSize = 15.sp)
                if (subtitle.isNotEmpty()) {
                    Text(text = subtitle, color = Color.Gray, fontSize = 13.sp)
                }
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(checkedThumbColor = Color.White),
            modifier = Modifier.padding(end = 16.dp)
        )
    }
}


@Composable
fun CustomWiFiSwitch() {
    var checked by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF121212)) // dark background
            .padding(vertical = 12.dp, horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Wifi,
                    contentDescription = "Wi-Fi Icon",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Wi-Fi Only",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }

            Switch(
                checked = checked,
                onCheckedChange = { checked = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    uncheckedThumbColor = Color.LightGray,
                    uncheckedTrackColor = Color.DarkGray,
                    checkedTrackColor = Color.Gray
                )
            )
        }
    }
}

@Composable
fun StorageBar(used: Float, netflix: Float, free: Float) {
    val total = used + netflix + free
    val usedWeight = used / total
    val netflixWeight = netflix / total
    val freeWeight = free / total

    Column(modifier = Modifier.padding(top = 16.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color.DarkGray)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(usedWeight)
                    .background(Color.White)
            )
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(netflixWeight)
                    .background(Color.Blue)
            )
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(freeWeight)
                    .background(Color.Gray)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Used • 65 GB   Netflix • 18 B   Free • 52 GB",
            color = Color.Gray,
            fontSize = 12.sp
        )
    }
}

