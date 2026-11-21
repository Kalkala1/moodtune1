package com.example.moodtune.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "moods")
data class MoodEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val moodType: String,
    val note: String,
    val intensity: Int,
    val date: Long = System.currentTimeMillis()
)
