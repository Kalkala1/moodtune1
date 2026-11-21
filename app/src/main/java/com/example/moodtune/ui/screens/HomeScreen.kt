package com.example.moodtune.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moodtune.ui.components.DailyMemoryCardDialog
import com.example.moodtune.ui.theme.CloudBlue
import com.example.moodtune.ui.theme.Lavender
import com.example.moodtune.ui.theme.Lilac
import com.example.moodtune.viewmodel.HomeViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToCheckIn: () -> Unit,
    onNavigateToJournal: () -> Unit,
    onNavigateToDiscover: () -> Unit
) {
    val latestMood by viewModel.latestMood.collectAsState()
    val streak by viewModel.streakCount.collectAsState()
    val songs by viewModel.allSongs.collectAsState()

    var showMemoryCard by remember { mutableStateOf(false) }

    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val greeting = when {
        hour < 12 -> "Good Morning ☀️"
        hour < 17 -> "Good Afternoon 🌤️"
        else -> "Good Evening 🌙"
    }

    val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault())
    val todayStr = dateFormat.format(Date())

    val recommendedSong = songs.firstOrNull { it.moodType.equals(latestMood?.moodType, ignoreCase = true) } ?: songs.firstOrNull()

    if (showMemoryCard) {
        DailyMemoryCardDialog(latestMood = latestMood, onDismiss = { showMemoryCard = false })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        // Header Greeting
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = greeting, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = todayStr, fontSize = 13.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
            }
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(44.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Daily Memory Card Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(Lavender.copy(alpha = 0.6f), CloudBlue.copy(alpha = 0.7f))
                    )
                )
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Daily Memory Card", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = latestMood?.let { "Mood Anda: ${it.moodType} (Intensitas ${it.intensity}/5)" } ?: "Belum ada check-in hari ini.",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Button(
                    onClick = { showMemoryCard = true },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Text("Buka", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Streak & Quick Check-in Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = Color(0xFFFF6B6B), modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(text = "$streak Hari", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text(text = "Mood Streak", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                    }
                }
            }

            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onNavigateToCheckIn() },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Lilac.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.AddReaction, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(text = "Check-in", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text(text = "Catat Mood", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Soundtrack recommendation based on mood
        Text(text = "Soundtrack Untukmu 🎵", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(10.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onNavigateToDiscover() },
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Lavender),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.MusicNote, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = recommendedSong?.title ?: "Pilih Soundtrack", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(text = recommendedSong?.artist ?: "Eksplor lagu di Discover", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                }
                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Journal Shortcut
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Mood Journal Harian", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            TextButton(onClick = onNavigateToJournal) {
                Text("Lihat Semua")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onNavigateToJournal() },
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Book, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "Tulis Catatan Diary", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(text = "Abadikan perasaan dan ceritamu hari ini", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                }
            }
        }
    }
}
