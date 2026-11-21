package com.example.moodtune.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moodtune.data.entity.MoodEntity
import com.example.moodtune.data.repository.MoodRepository
import kotlinx.coroutines.launch

class CheckInViewModel(
    private val moodRepository: MoodRepository
) : ViewModel() {

    var selectedMood by mutableStateOf("Calm")
        private set

    var note by mutableStateOf("")
        private set

    var intensity by mutableIntStateOf(3)
        private set

    var isSavedSuccessfully by mutableStateOf(false)
        private set

    fun onMoodSelected(mood: String) {
        selectedMood = mood
    }

    fun onNoteChanged(newNote: String) {
        note = newNote
    }

    fun onIntensityChanged(newIntensity: Int) {
        intensity = newIntensity
    }

    fun saveCheckIn(onComplete: () -> Unit) {
        viewModelScope.launch {
            val moodEntity = MoodEntity(
                moodType = selectedMood,
                note = note,
                intensity = intensity,
                date = System.currentTimeMillis()
            )
            moodRepository.insertMood(moodEntity)
            isSavedSuccessfully = true
            onComplete()
        }
    }

    fun resetState() {
        selectedMood = "Calm"
        note = ""
        intensity = 3
        isSavedSuccessfully = false
    }
}
