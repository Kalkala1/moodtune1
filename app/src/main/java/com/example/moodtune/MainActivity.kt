package com.example.moodtune

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.moodtune.data.database.AppDatabase
import com.example.moodtune.data.entity.SongEntity
import com.example.moodtune.data.repository.JournalRepository
import com.example.moodtune.data.repository.MoodRepository
import com.example.moodtune.data.repository.SongRepository
import com.example.moodtune.navigation.NavGraph
import com.example.moodtune.sensor.LightSensorManager
import com.example.moodtune.sensor.ShakeSensorManager
import com.example.moodtune.ui.components.BottomNavigationBar
import com.example.moodtune.ui.theme.MoodTuneTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var shakeSensorManager: ShakeSensorManager
    private lateinit var lightSensorManager: LightSensorManager
    private lateinit var database: AppDatabase

    var shuffledSong by mutableStateOf<SongEntity?>(null)
    var showShakeDialog by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        database = AppDatabase.getDatabase(applicationContext, lifecycleScope)
        val moodRepository = MoodRepository(database.moodDao())
        val songRepository = SongRepository(database.songDao())
        val journalRepository = JournalRepository(database.journalDao())

        shakeSensorManager = ShakeSensorManager(this)
        lightSensorManager = LightSensorManager(this)

        setContent {
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            var isDimmedMode by remember { mutableStateOf(false) }

            // Observe light sensor lux for auto theme dimming
            val lux by lightSensorManager.luxState.collectAsState()
            LaunchedEffect(lux) {
                isDimmedMode = lux < LightSensorManager.DIMMED_LUX_THRESHOLD
            }

            MoodTuneTheme(darkTheme = isDimmedMode) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (currentRoute in listOf("home", "checkin", "discover", "history", "profile")) {
                            BottomNavigationBar(
                                currentRoute = currentRoute,
                                onItemClick = { item ->
                                    if (currentRoute != item.route) {
                                        navController.navigate(item.route) {
                                            popUpTo(navController.graph.startDestinationId) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                }
                            )
                        }
                    }
                ) { innerPadding ->
                    NavGraph(
                        navController = navController,
                        moodRepository = moodRepository,
                        songRepository = songRepository,
                        journalRepository = journalRepository,
                        modifier = Modifier.padding(innerPadding)
                    )

                    if (showShakeDialog && shuffledSong != null) {
                        AlertDialog(
                            onDismissRequest = { showShakeDialog = false },
                            title = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Headphones, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("✨ Shake Shuffle Discovery!", fontWeight = FontWeight.Bold)
                                }
                            },
                            text = {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f))
                                        .padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(text = "🎵", fontSize = 36.sp)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(text = shuffledSong!!.title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.onSurface)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = shuffledSong!!.artist, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = MaterialTheme.colorScheme.primary
                                    ) {
                                        Text(
                                            text = "Mood: ${shuffledSong!!.moodType}",
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onPrimary,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            },
                            confirmButton = {
                                Button(onClick = { showShakeDialog = false }) {
                                    Text("Putar / Tutup")
                                }
                            },
                            shape = RoundedCornerShape(24.dp)
                        )
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        shakeSensorManager.startListening {
            lifecycleScope.launch {
                try {
                    val songs = database.songDao().getAllSongs().first()
                    if (songs.isNotEmpty()) {
                        shuffledSong = songs.random()
                        showShakeDialog = true
                        Toast.makeText(this@MainActivity, "✨ Lagu di-shuffle via Shake!", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, "Gagal shuffle lagu", Toast.LENGTH_SHORT).show()
                }
            }
        }
        lightSensorManager.startListening()
    }

    override fun onPause() {
        super.onPause()
        shakeSensorManager.stopListening()
        lightSensorManager.stopListening()
    }
}
