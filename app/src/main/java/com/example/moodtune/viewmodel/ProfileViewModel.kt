package com.example.moodtune.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {

    var profileName by mutableStateOf("Aesthetic Dreamer")
        private set

    var isAutoThemeEnabled by mutableStateOf(true)
        private set

    var isDarkModeOverride by mutableStateOf(false)
        private set

    var accelerometerSensitivity by mutableStateOf("Normal")
        private set

    fun updateProfileName(name: String) {
        profileName = name
    }

    fun toggleAutoTheme(enabled: Boolean) {
        isAutoThemeEnabled = enabled
    }

    fun toggleDarkModeOverride(dark: Boolean) {
        isDarkModeOverride = dark
    }

    fun setSensorSensitivity(sensitivity: String) {
        accelerometerSensitivity = sensitivity
    }
}
