package com.example.moodtune.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moodtune.ui.components.SongCard
import com.example.moodtune.ui.theme.Lavender
import com.example.moodtune.viewmodel.DiscoverViewModel

@Composable
fun DiscoverScreen(
    viewModel: DiscoverViewModel
) {
    val context = LocalContext.current
    val songs by viewModel.filteredSongs.collectAsState()
    val selectedMood by viewModel.selectedMoodFilter.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    var newTitle by remember { mutableStateOf("") }
    var newArtist by remember { mutableStateOf("") }
    var newMood by remember { mutableStateOf("Calm") }

    val moods = listOf("All", "Happy", "Calm", "Sad", "Angry", "Anxious", "Excited")

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Tambah Soundtrack Baru", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = newTitle,
                        onValueChange = { newTitle = it },
                        label = { Text("Judul Lagu") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = newArtist,
                        onValueChange = { newArtist = it },
                        label = { Text("Penyanyi / Artist") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = newMood,
                        onValueChange = { newMood = it },
                        label = { Text("Kategori Mood (Happy, Calm, dll)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (newTitle.isNotBlank() && newArtist.isNotBlank()) {
                        viewModel.addSong(newTitle, newArtist, newMood)
                        newTitle = ""
                        newArtist = ""
                        showAddDialog = false
                        Toast.makeText(context, "Lagu berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
                    }
                }) {
                    Text("Simpan")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) { Text("Batal") }
            },
            shape = RoundedCornerShape(20.dp)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "Mood & Soundtrack", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(text = "Rekomendasi lagu terbaik untuk suasana hatimu", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            }
            IconButton(
                onClick = { showAddDialog = true },
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Lagu", tint = MaterialTheme.colorScheme.primary)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Shake Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Lavender.copy(alpha = 0.3f))
                .padding(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.PhoneAndroid, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Goyang (shake) HP-mu untuk putar rekomendasi lagu acak!",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.setSearchQuery(it) },
            label = { Text("Cari lagu atau artis...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Mood Filter Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(moods) { mood ->
                FilterChip(
                    selected = selectedMood == mood,
                    onClick = { viewModel.setMoodFilter(mood) },
                    label = { Text(mood) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Song List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(songs, key = { it.id }) { song ->
                SongCard(
                    song = song,
                    onFavoriteClick = { viewModel.toggleFavorite(song) },
                    onDeleteClick = { viewModel.deleteSong(song) }
                )
            }
        }
    }
}
