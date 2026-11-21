package com.example.moodtune.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object CheckIn : Screen("checkin")
    object Discover : Screen("discover")
    object Journal : Screen("journal")
    object History : Screen("history")
    object Profile : Screen("profile")
}
