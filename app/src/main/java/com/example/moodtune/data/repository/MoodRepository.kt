package com.example.moodtune.data.repository

import com.example.moodtune.data.dao.MoodDao
import com.example.moodtune.data.entity.MoodEntity
import kotlinx.coroutines.flow.Flow

class MoodRepository(private val moodDao: MoodDao) {
    val allMoods: Flow<List<MoodEntity>> = moodDao.getAllMoods()
    val latestMood: Flow<MoodEntity?> = moodDao.getLatestMood()
    val moodCount: Flow<Int> = moodDao.getMoodCount()

    suspend fun insertMood(mood: MoodEntity) {
        moodDao.insertMood(mood)
    }

    suspend fun updateMood(mood: MoodEntity) {
        moodDao.updateMood(mood)
    }

    suspend fun deleteMood(mood: MoodEntity) {
        moodDao.deleteMood(mood)
    }

    fun getMoodsBetweenDates(startDate: Long, endDate: Long): Flow<List<MoodEntity>> {
        return moodDao.getMoodsBetweenDates(startDate, endDate)
    }
}
