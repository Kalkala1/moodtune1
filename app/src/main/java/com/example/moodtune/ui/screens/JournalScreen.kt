package com.example.moodtune.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moodtune.ui.components.JournalCard
import com.example.moodtune.viewmodel.JournalViewModel

@Composable
fun JournalScreen(
    viewModel: JournalViewModel
) {
    val context = LocalContext.current
    val journals by viewModel.allJournals.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var moodType by remember { mutableStateOf("Calm") }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Tulis Jurnal Baru", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Judul Jurnal") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = moodType,
                        onValueChange = { moodType = it },
                        label = { Text("Mood Terkait (Happy, Calm, dll)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    OutlinedTextField(
                        value = content,
                        onValueChange = { content = it },
                        label = { Text("Ceritakan harimu...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (title.isNotBlank() && content.isNotBlank()) {
                        viewModel.addJournal(title, content, moodType)
                        title = ""
                        content = ""
                        showAddDialog = false
                        Toast.makeText(context, "Jurnal berhasil disimpan!", Toast.LENGTH_SHORT).show()
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

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Jurnal")
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(text = "Mood Journal 📖", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Catat cerita dan refleksi harianmu di sini", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))

            Spacer(modifier = Modifier.height(16.dp))

            if (journals.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Belum ada catatan jurnal. Ketuk tombol + untuk mulai menulis.", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(journals, key = { it.id }) { journal ->
                        JournalCard(
                            journal = journal,
                            onDeleteClick = { viewModel.deleteJournal(journal) }
                        )
                    }
                }
            }
        }
    }
}
