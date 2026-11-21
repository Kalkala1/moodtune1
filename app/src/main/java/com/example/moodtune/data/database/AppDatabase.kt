package com.example.moodtune.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.moodtune.data.dao.JournalDao
import com.example.moodtune.data.dao.MoodDao
import com.example.moodtune.data.dao.SongDao
import com.example.moodtune.data.entity.JournalEntity
import com.example.moodtune.data.entity.MoodEntity
import com.example.moodtune.data.entity.SongEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [MoodEntity::class, SongEntity::class, JournalEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun moodDao(): MoodDao
    abstract fun songDao(): SongDao
    abstract fun journalDao(): JournalDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "moodtune_database"
                )
                .fallbackToDestructiveMigration()
                .addCallback(AppDatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class AppDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateInitialSongs(database.songDao())
                    populateInitialJournal(database.journalDao())
                }
            }
        }

        suspend fun populateInitialSongs(songDao: SongDao) {
            val sampleSongs = listOf(
                SongEntity(title = "Sunshine Melody", artist = "Lofi Dreamer", moodType = "Happy", isFavorite = true),
                SongEntity(title = "Good Vibes Only", artist = "Sunset Beats", moodType = "Happy", isFavorite = false),
                SongEntity(title = "Weightless Water", artist = "Ambient Chill", moodType = "Calm", isFavorite = true),
                SongEntity(title = "Peaceful Breeze", artist = "Quiet Mind", moodType = "Calm", isFavorite = false),
                SongEntity(title = "Rainy Window", artist = "Soft Keys", moodType = "Sad", isFavorite = false),
                SongEntity(title = "Echoes of Yesterday", artist = "Midnight Melancholy", moodType = "Sad", isFavorite = true),
                SongEntity(title = "Thunder Rage", artist = "Heavy Beats", moodType = "Angry", isFavorite = false),
                SongEntity(title = "Unstoppable Force", artist = "Energy Core", moodType = "Angry", isFavorite = false),
                SongEntity(title = "Breathing Room", artist = "Calm Frequency", moodType = "Anxious", isFavorite = true),
                SongEntity(title = "Gentle Waves", artist = "Ocean Whispers", moodType = "Anxious", isFavorite = false),
                SongEntity(title = "Starry Night Jump", artist = "Electric Disco", moodType = "Excited", isFavorite = true),
                SongEntity(title = "Cosmic Pulse", artist = "Neon Glow", moodType = "Excited", isFavorite = false)
            )
            songDao.insertSongs(sampleSongs)
        }

        suspend fun populateInitialJournal(journalDao: JournalDao) {
            val initialJournal = JournalEntity(
                title = "Selamat Datang di MoodTune!",
                content = "Hari ini adalah hari pertama menggunakan MoodTune. Semoga aplikasi ini bisa membantuku lebih memahami suasana hati dan menemukan soundtrack lagu terbaik setiap harinya! ✨🎵",
                moodType = "Calm",
                date = System.currentTimeMillis()
            )
            journalDao.insertJournal(initialJournal)
        }
    }
}
