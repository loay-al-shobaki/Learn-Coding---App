package com.example.learningcodingapp.ui.HomeScreen




import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.learningcodingapp.core.navigation.Routes
import com.example.learningcodingapp.ui.QuizzScreen.QuizzScreen
import com.example.learningcodingapp.ui.RankScreen.RankScreen
import com.example.learningcodingapp.ui.SettingsScreen.SettingsScreen

data class NavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

@Composable
fun MainScreen(outerNav: NavHostController) {
    val tabsNav = rememberNavController()

    val items = remember {
        listOf(
            NavItem(Routes.HOME,     "Home",     Icons.Filled.Home),
            NavItem(Routes.QUIZZ,    "Quizz",    Icons.Filled.Article),
            NavItem(Routes.RANK,     "Rank",     Icons.Filled.EmojiEvents),
            NavItem(Routes.SETTINGS, "Settings", Icons.Filled.Settings)
        )
    }

    val backStackEntry by tabsNav.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: Routes.HOME

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEach { item ->
                    val selected = currentRoute == item.route
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            if (!selected) {
                                tabsNav.navigate(item.route) {
                                    popUpTo(Routes.HOME) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = tabsNav,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(padding)
        ) {
            composable(Routes.HOME)  { HomeScreen() }
            composable(Routes.QUIZZ) { QuizzScreen() }
            composable(Routes.RANK)  { RankScreen() }
            composable(Routes.SETTINGS) {
                SettingsScreen(
                    onEditProfile = { outerNav.navigate(Routes.EDIT_PROFILE) } // ← انتقال للغراف الخارجي
                )
            }
        }
    }
}
