package com.example.moodtune.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moodtune.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel
) {
    var isAutoTheme by remember { mutableStateOf(viewModel.isAutoThemeEnabled) }
    var isDarkOverride by remember { mutableStateOf(viewModel.isDarkModeOverride) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        // Avatar
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(50.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text(text = viewModel.profileName, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text(text = "Mood & Soundtrack Enthusiast", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))

        Spacer(modifier = Modifier.height(30.dp))

        // Settings Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Pengaturan Sensor & Tema", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.WbSunny, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(text = "Auto Light Sensor Theme", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                            Text(text = "Sesuaikan tema dengan cahaya sekitar", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                        }
                    }
                    Switch(
                        checked = isAutoTheme,
                        onCheckedChange = {
                            isAutoTheme = it
                            viewModel.toggleAutoTheme(it)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(text = "Dark Mode Override", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                            Text(text = "Paksa tema gelap aktif", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                        }
                    }
                    Switch(
                        checked = isDarkOverride,
                        onCheckedChange = {
                            isDarkOverride = it
                            viewModel.toggleDarkModeOverride(it)
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // App Info Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Tentang Aplikasi", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "MOODTUNE — Your Mood, Your Soundtrack", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Text(text = "Versi 1.0.0 • Dibangun dengan Jetpack Compose & Room Database", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            }
        }
    }
}
