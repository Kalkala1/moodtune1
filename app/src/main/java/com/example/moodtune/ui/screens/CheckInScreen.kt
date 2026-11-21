package com.example.moodtune.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moodtune.ui.components.MoodCardItem
import com.example.moodtune.ui.components.moodOptionsList
import com.example.moodtune.viewmodel.CheckInViewModel

@Composable
fun CheckInScreen(
    viewModel: CheckInViewModel,
    onSavedSuccessfully: () -> Unit
) {
    var note by remember { mutableStateOf("") }
    var intensity by remember { mutableFloatStateOf(3f) }
    var selectedMood by remember { mutableStateOf("Calm") }
    var showSuccessSnackbar by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "Bagaimana Perasaanmu?", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = "Pilih suasana hati yang paling mewakili dirimu saat ini.", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))

        Spacer(modifier = Modifier.height(24.dp))

        // Mood Grid Selection
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val rows = moodOptionsList.chunked(3)
            rows.forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    rowItems.forEach { option ->
                        MoodCardItem(
                            mood = option,
                            isSelected = selectedMood == option.name,
                            onClick = { selectedMood = option.name }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Intensity Slider
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Intensitas Mood", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(text = "${intensity.toInt()} / 5", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Slider(
                    value = intensity,
                    onValueChange = { intensity = it },
                    valueRange = 1f..5f,
                    steps = 3,
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary,
                        activeTrackColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Note Input
        OutlinedTextField(
            value = note,
            onValueChange = { note = it },
            label = { Text("Tambahkan catatan singkat (opsional)...") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            shape = RoundedCornerShape(16.dp)
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Save Button
        Button(
            onClick = {
                viewModel.onMoodSelected(selectedMood)
                viewModel.onIntensityChanged(intensity.toInt())
                viewModel.onNoteChanged(note)
                viewModel.saveCheckIn {
                    showSuccessSnackbar = true
                    onSavedSuccessfully()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Icon(Icons.Default.Check, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Simpan Check-in", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}
