package com.example.moodtune.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moodtune.viewmodel.HistoryViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel
) {
    val moods by viewModel.allMoods.collectAsState()
    val totalCheckIns by viewModel.totalCheckIns.collectAsState()
    val topMood by viewModel.topMood.collectAsState()
    val streak by viewModel.streakCount.collectAsState()

    val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Riwayat & Statistik", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Perkembangan suasana hatimu dari waktu ke waktu", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))

        Spacer(modifier = Modifier.height(16.dp))

        // Stats Cards Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.BarChart, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "$totalCheckIns", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Total Check-in", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                }
            }

            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = Color(0xFFFF6B6B))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "$streak Hari", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Streak", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                }
            }

            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Favorite, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = topMood, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Top Mood", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "Daftar Riwayat Check-in", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(10.dp))

        if (moods.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Belum ada riwayat check-in.", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(moods, key = { it.id }) { mood ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = when (mood.moodType) {
                                        "Happy" -> "😊"
                                        "Calm" -> "🌿"
                                        "Sad" -> "🌧️"
                                        "Angry" -> "🔥"
                                        "Anxious" -> "🌊"
                                        else -> "🌟"
                                    },
                                    fontSize = 32.sp
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(text = mood.moodType, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    if (mood.note.isNotBlank()) {
                                        Text(text = mood.note, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(text = dateFormat.format(Date(mood.date)), fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                                }
                            }
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.primaryContainer
                            ) {
                                Text(
                                    text = "Intensitas ${mood.intensity}/5",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
