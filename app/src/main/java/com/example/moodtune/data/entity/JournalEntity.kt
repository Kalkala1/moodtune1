package com.example.moodtune.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "journals")
data class JournalEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val content: String,
    val moodType: String,
    val date: Long = System.currentTimeMillis()
)
