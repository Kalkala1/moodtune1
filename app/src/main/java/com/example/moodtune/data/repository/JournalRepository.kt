package com.example.moodtune.data.repository

import com.example.moodtune.data.dao.JournalDao
import com.example.moodtune.data.entity.JournalEntity
import kotlinx.coroutines.flow.Flow

class JournalRepository(private val journalDao: JournalDao) {
    val allJournals: Flow<List<JournalEntity>> = journalDao.getAllJournals()

    suspend fun insertJournal(journal: JournalEntity) {
        journalDao.insertJournal(journal)
    }

    suspend fun updateJournal(journal: JournalEntity) {
        journalDao.updateJournal(journal)
    }

    suspend fun deleteJournal(journal: JournalEntity) {
        journalDao.deleteJournal(journal)
    }

    fun getJournalsByMood(moodType: String): Flow<List<JournalEntity>> {
        return journalDao.getJournalsByMood(moodType)
    }
}
