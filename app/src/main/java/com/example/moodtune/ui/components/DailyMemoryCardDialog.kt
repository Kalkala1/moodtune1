package com.example.moodtune.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moodtune.data.entity.MoodEntity
import com.example.moodtune.ui.theme.CloudBlue
import com.example.moodtune.ui.theme.Lavender
import com.example.moodtune.ui.theme.Lilac
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DailyMemoryCardDialog(
    latestMood: MoodEntity?,
    onDismiss: () -> Unit
) {
    val dateFormat = SimpleDateFormat("EEEE, dd MMM yyyy", Locale.getDefault())
    val dateStr = latestMood?.let { dateFormat.format(Date(it.date)) } ?: dateFormat.format(Date())

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Tutup", fontWeight = FontWeight.Bold)
            }
        },
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Daily Memory Card", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Lavender.copy(alpha = 0.5f), CloudBlue.copy(alpha = 0.6f), Lilac.copy(alpha = 0.4f))
                        )
                    )
                    .padding(20.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "MOODTUNE MEMORY",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = latestMood?.moodType?.let { mood ->
                            when (mood) {
                                "Happy" -> "😊"
                                "Calm" -> "🌿"
                                "Sad" -> "🌧️"
                                "Angry" -> "🔥"
                                "Anxious" -> "🌊"
                                else -> "🌟"
                            }
                        } ?: "🌟",
                        fontSize = 48.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = latestMood?.moodType ?: "Belum ada Check-in",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = dateStr,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )

                    if (!latestMood?.note.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "\"${latestMood?.note}\"",
                                modifier = Modifier.padding(12.dp),
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        },
        shape = RoundedCornerShape(28.dp)
    )
}
