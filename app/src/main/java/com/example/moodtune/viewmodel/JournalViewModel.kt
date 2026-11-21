package com.example.moodtune.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moodtune.data.entity.JournalEntity
import com.example.moodtune.data.repository.JournalRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class JournalViewModel(
    private val journalRepository: JournalRepository
) : ViewModel() {

    private val _selectedMoodFilter = MutableStateFlow("All")
    val selectedMoodFilter: StateFlow<String> = _selectedMoodFilter.asStateFlow()

    val allJournals: StateFlow<List<JournalEntity>> = journalRepository.allJournals
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredJournals: StateFlow<List<JournalEntity>> = combine(allJournals, _selectedMoodFilter) { journals, mood ->
        if (mood == "All") journals else journals.filter { it.moodType.equals(mood, ignoreCase = true) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setMoodFilter(mood: String) {
        _selectedMoodFilter.value = mood
    }

    fun addJournal(title: String, content: String, moodType: String) {
        viewModelScope.launch {
            val journal = JournalEntity(
                title = title,
                content = content,
                moodType = moodType,
                date = System.currentTimeMillis()
            )
            journalRepository.insertJournal(journal)
        }
    }

    fun updateJournal(journal: JournalEntity) {
        viewModelScope.launch {
            journalRepository.updateJournal(journal)
        }
    }

    fun deleteJournal(journal: JournalEntity) {
        viewModelScope.launch {
            journalRepository.deleteJournal(journal)
        }
    }
}
