package com.example.moodtune.data.dao

import androidx.room.*
import com.example.moodtune.data.entity.SongEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SongDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSong(song: SongEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSongs(songs: List<SongEntity>)

    @Update
    suspend fun updateSong(song: SongEntity)

    @Delete
    suspend fun deleteSong(song: SongEntity)

    @Query("SELECT * FROM songs ORDER BY id DESC")
    fun getAllSongs(): Flow<List<SongEntity>>

    @Query("SELECT * FROM songs WHERE moodType = :moodType ORDER BY id DESC")
    fun getSongsByMood(moodType: String): Flow<List<SongEntity>>

    @Query("SELECT * FROM songs WHERE isFavorite = 1 ORDER BY id DESC")
    fun getFavoriteSongs(): Flow<List<SongEntity>>

    @Query("SELECT COUNT(*) FROM songs")
    suspend fun getSongCount(): Int
}
