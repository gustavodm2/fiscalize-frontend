import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.fiscalize.activities.HistoryActivity
import com.example.fiscalize.activities.HomeContent
import com.example.fiscalize.activities.LoginActivity
import com.example.fiscalize.activities.SimplesActivity
import com.example.fiscalize.ui.theme.mainRed
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation


@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val systemUiController = rememberSystemUiController()
    val navController = rememberNavController()

    systemUiController.setStatusBarColor(
        color = mainRed
    )

    NavHost(navController = navController, startDestination = "login", builder = {
        composable("home") { HomeContent(modifier, navController) }
        composable("login") { LoginActivity(modifier, navController) }
        navigation(startDestination = "dashboard", route = "main") {
            composable("dashboard") {
                BottomTabNavigation(modifier)
            }
            composable("taxes") {
                SimplesActivity(modifier, navController)
            }
        }
    })
}

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun BottomTabNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    val tabs = listOf(
        BottomNavItem("Dashboard", "dashboard", com.example.fiscalize.R.drawable.ic_dashboard),
        BottomNavItem("Taxes", "taxes", com.example.fiscalize.R.drawable.ic_history)
    )

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController, items = tabs)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = tabs[0].route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("dashboard") { HistoryActivity(modifier, navController) }
            composable("taxes") { SimplesActivity(modifier, navController) }
        }
    }
}


@Composable
fun BottomNavigationBar(navController: NavController, items: List<BottomNavItem>) {
    BottomNavigation(
        backgroundColor = mainRed,
        contentColor = Color.White
    ) {
        val currentRoute = currentRoute(navController)
        items.forEach { item ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.label
                    )
                },
                label = { Text(text = item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun currentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}

data class BottomNavItem(
    val label: String,
    val route: String,
    val icon: Int
)
