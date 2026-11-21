package com.example.moodtune.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.moodtune.navigation.Screen

sealed class NavigationItem(val route: String, val title: String, val icon: ImageVector) {
    object Home : NavigationItem(Screen.Home.route, "Home", Icons.Default.Home)
    object CheckIn : NavigationItem(Screen.CheckIn.route, "Check-in", Icons.Default.Favorite)
    object Discover : NavigationItem(Screen.Discover.route, "Discover", Icons.Default.Headphones)
    object History : NavigationItem(Screen.History.route, "History", Icons.Default.BarChart)
    object Profile : NavigationItem(Screen.Profile.route, "Profile", Icons.Default.Person)
}

@Composable
fun BottomNavigationBar(
    currentRoute: String?,
    onItemClick: (NavigationItem) -> Unit
) {
    val items = listOf(
        NavigationItem.Home,
        NavigationItem.CheckIn,
        NavigationItem.Discover,
        NavigationItem.History,
        NavigationItem.Profile
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.primary
    ) {
        items.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = selected,
                onClick = { onItemClick(item) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    }
}
