package com.example.moodtune.data.repository

import com.example.moodtune.data.dao.SongDao
import com.example.moodtune.data.entity.SongEntity
import kotlinx.coroutines.flow.Flow

class SongRepository(private val songDao: SongDao) {
    val allSongs: Flow<List<SongEntity>> = songDao.getAllSongs()
    val favoriteSongs: Flow<List<SongEntity>> = songDao.getFavoriteSongs()

    suspend fun insertSong(song: SongEntity) {
        songDao.insertSong(song)
    }

    suspend fun updateSong(song: SongEntity) {
        songDao.updateSong(song)
    }

    suspend fun deleteSong(song: SongEntity) {
        songDao.deleteSong(song)
    }

    fun getSongsByMood(moodType: String): Flow<List<SongEntity>> {
        return songDao.getSongsByMood(moodType)
    }
}
