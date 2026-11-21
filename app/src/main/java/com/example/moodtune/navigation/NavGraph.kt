package com.example.moodtune.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.moodtune.data.repository.JournalRepository
import com.example.moodtune.data.repository.MoodRepository
import com.example.moodtune.data.repository.SongRepository
import com.example.moodtune.ui.screens.*
import com.example.moodtune.viewmodel.*

@Composable
fun NavGraph(
    navController: NavHostController,
    moodRepository: MoodRepository,
    songRepository: SongRepository,
    journalRepository: JournalRepository,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            val viewModel = HomeViewModel(moodRepository, songRepository, journalRepository)
            HomeScreen(
                viewModel = viewModel,
                onNavigateToCheckIn = { navController.navigate(Screen.CheckIn.route) },
                onNavigateToJournal = { navController.navigate(Screen.Journal.route) },
                onNavigateToDiscover = { navController.navigate(Screen.Discover.route) }
            )
        }
        composable(Screen.CheckIn.route) {
            val viewModel = CheckInViewModel(moodRepository)
            CheckInScreen(
                viewModel = viewModel,
                onSavedSuccessfully = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Discover.route) {
            val viewModel = DiscoverViewModel(songRepository)
            DiscoverScreen(viewModel = viewModel)
        }
        composable(Screen.Journal.route) {
            val viewModel = JournalViewModel(journalRepository)
            JournalScreen(viewModel = viewModel)
        }
        composable(Screen.History.route) {
            val viewModel = HistoryViewModel(moodRepository)
            HistoryScreen(viewModel = viewModel)
        }
        composable(Screen.Profile.route) {
            val viewModel = ProfileViewModel()
            ProfileScreen(viewModel = viewModel)
        }
    }
}
