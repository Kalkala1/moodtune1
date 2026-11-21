package com.example.moodtune.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moodtune.data.entity.MoodEntity
import com.example.moodtune.data.repository.MoodRepository
import kotlinx.coroutines.flow.*

class HistoryViewModel(
    moodRepository: MoodRepository
) : ViewModel() {

    val allMoods: StateFlow<List<MoodEntity>> = moodRepository.allMoods
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalCheckIns: StateFlow<Int> = moodRepository.moodCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val topMood: StateFlow<String> = allMoods.map { moods ->
        if (moods.isEmpty()) "None"
        else moods.groupingBy { it.moodType }.eachCount().maxByOrNull { it.value }?.key ?: "Calm"
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "Calm")

    val streakCount: StateFlow<Int> = allMoods.map { moods ->
        if (moods.isEmpty()) 0
        else moods.distinctBy {
            val cal = java.util.Calendar.getInstance()
            cal.timeInMillis = it.date
            "${cal.get(java.util.Calendar.YEAR)}-${cal.get(java.util.Calendar.DAY_OF_YEAR)}"
        }.size
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
}
