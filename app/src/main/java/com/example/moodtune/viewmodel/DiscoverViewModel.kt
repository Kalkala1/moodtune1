package com.example.moodtune.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moodtune.data.entity.SongEntity
import com.example.moodtune.data.repository.SongRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DiscoverViewModel(
    private val songRepository: SongRepository
) : ViewModel() {

    private val _selectedMoodFilter = MutableStateFlow("All")
    val selectedMoodFilter: StateFlow<String> = _selectedMoodFilter.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val allSongs: StateFlow<List<SongEntity>> = songRepository.allSongs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredSongs: StateFlow<List<SongEntity>> = combine(allSongs, _selectedMoodFilter, _searchQuery) { songs, mood, query ->
        songs.filter { song ->
            val matchesMood = mood == "All" || song.moodType.equals(mood, ignoreCase = true)
            val matchesQuery = query.isBlank() || song.title.contains(query, ignoreCase = true) || song.artist.contains(query, ignoreCase = true)
            matchesMood && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setMoodFilter(mood: String) {
        _selectedMoodFilter.value = mood
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleFavorite(song: SongEntity) {
        viewModelScope.launch {
            songRepository.updateSong(song.copy(isFavorite = !song.isFavorite))
        }
    }

    fun addSong(title: String, artist: String, moodType: String, cover: String = "") {
        viewModelScope.launch {
            val song = SongEntity(
                title = title,
                artist = artist,
                moodType = moodType,
                cover = cover,
                isFavorite = false
            )
            songRepository.insertSong(song)
        }
    }

    fun deleteSong(song: SongEntity) {
        viewModelScope.launch {
            songRepository.deleteSong(song)
        }
    }

    fun pickRandomSong(): SongEntity? {
        val currentList = filteredSongs.value
        if (currentList.isEmpty()) return null
        return currentList.random()
    }
}
