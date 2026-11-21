package com.example.moodtune.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moodtune.ui.theme.*

data class MoodOption(val name: String, val emoji: String, val color: Color, val bg: Color)

val moodOptionsList = listOf(
    MoodOption("Happy", "😊", MoodHappy, MoodHappyBg),
    MoodOption("Calm", "🌿", MoodCalm, MoodCalmBg),
    MoodOption("Sad", "🌧️", MoodSad, MoodSadBg),
    MoodOption("Angry", "🔥", MoodAngry, MoodAngryBg),
    MoodOption("Anxious", "🌊", MoodAnxious, MoodAnxiousBg),
    MoodOption("Excited", "🌟", MoodExcited, MoodExcitedBg)
)

@Composable
fun MoodCardItem(
    mood: MoodOption,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(targetValue = if (isSelected) 1.08f else 1f, label = "mood_scale")
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) mood.color.copy(alpha = 0.4f) else mood.bg,
        label = "mood_bg"
    )

    Card(
        modifier = Modifier
            .width(100.dp)
            .height(110.dp)
            .scale(scale)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 6.dp else 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = mood.emoji, fontSize = 32.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = mood.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = DeepPlum
            )
        }
    }
}
