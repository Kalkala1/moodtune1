package com.example.moodtune.data.dao

import androidx.room.*
import com.example.moodtune.data.entity.MoodEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MoodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMood(mood: MoodEntity)

    @Update
    suspend fun updateMood(mood: MoodEntity)

    @Delete
    suspend fun deleteMood(mood: MoodEntity)

    @Query("SELECT * FROM moods ORDER BY date DESC")
    fun getAllMoods(): Flow<List<MoodEntity>>

    @Query("SELECT * FROM moods ORDER BY date DESC LIMIT 1")
    fun getLatestMood(): Flow<MoodEntity?>

    @Query("SELECT * FROM moods WHERE date BETWEEN :startDate AND :endDate ORDER BY date DESC")
    fun getMoodsBetweenDates(startDate: Long, endDate: Long): Flow<List<MoodEntity>>

    @Query("SELECT COUNT(*) FROM moods")
    fun getMoodCount(): Flow<Int>
}
