package com.example.moodtune.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moodtune.data.entity.JournalEntity
import com.example.moodtune.data.entity.MoodEntity
import com.example.moodtune.data.entity.SongEntity
import com.example.moodtune.data.repository.JournalRepository
import com.example.moodtune.data.repository.MoodRepository
import com.example.moodtune.data.repository.SongRepository
import kotlinx.coroutines.flow.*

class HomeViewModel(
    moodRepository: MoodRepository,
    songRepository: SongRepository,
    journalRepository: JournalRepository
) : ViewModel() {

    val latestMood: StateFlow<MoodEntity?> = moodRepository.latestMood
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val moodCount: StateFlow<Int> = moodRepository.moodCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val allSongs: StateFlow<List<SongEntity>> = songRepository.allSongs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allJournals: StateFlow<List<JournalEntity>> = journalRepository.allJournals
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val streakCount: StateFlow<Int> = moodRepository.allMoods
        .map { moods -> computeStreak(moods) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    private fun computeStreak(moods: List<MoodEntity>): Int {
        if (moods.isEmpty()) return 0
        // Simple streak calculation based on consecutive days check-in
        return moods.distinctBy {
            val cal = java.util.Calendar.getInstance()
            cal.timeInMillis = it.date
            "${cal.get(java.util.Calendar.YEAR)}-${cal.get(java.util.Calendar.DAY_OF_YEAR)}"
        }.size
    }
}
