package com.example.moodtune.data.dao

import androidx.room.*
import com.example.moodtune.data.entity.JournalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface JournalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJournal(journal: JournalEntity)

    @Update
    suspend fun updateJournal(journal: JournalEntity)

    @Delete
    suspend fun deleteJournal(journal: JournalEntity)

    @Query("SELECT * FROM journals ORDER BY date DESC")
    fun getAllJournals(): Flow<List<JournalEntity>>

    @Query("SELECT * FROM journals WHERE id = :id")
    fun getJournalById(id: Int): Flow<JournalEntity?>

    @Query("SELECT * FROM journals WHERE moodType = :moodType ORDER BY date DESC")
    fun getJournalsByMood(moodType: String): Flow<List<JournalEntity>>
}
