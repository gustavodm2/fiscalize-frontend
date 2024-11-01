package com.example.fiscalize.routes

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.fiscalize.activities.HistoryActivity
import com.example.fiscalize.activities.LoginActivity
import com.example.fiscalize.ui.theme.mainRed
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import androidx.navigation.navigation
import com.example.fiscalize.R
import com.example.fiscalize.activities.DocDetailActivity
import com.example.fiscalize.activities.GraphActivity
import com.example.fiscalize.activities.HomeActivity
import com.example.fiscalize.activities.TaxDetailActivity
import com.example.fiscalize.model.api.SessionManager
import com.example.fiscalize.viewModel.LoginViewModel
import com.example.fiscalize.viewModel.GraphViewModel

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val systemUiController = rememberSystemUiController()
    val navController = rememberNavController()
    val graphViewModel: GraphViewModel = viewModel()
    val loginViewModel: LoginViewModel = viewModel()
    val context: Context = LocalContext.current
    val sessionManager: SessionManager = SessionManager(context)
    var userToken by remember { mutableStateOf<String?>(null) }

    systemUiController.setStatusBarColor(
        color = mainRed
    )

    LaunchedEffect(Unit) {
        userToken = sessionManager.fetchAuthToken()
    }

    val startDestination = if (userToken.isNullOrEmpty()) "login" else "home"

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("home") { HomeActivity(modifier, navController) }
        composable("login") {
            LoginActivity(modifier, navController, loginViewModel)
            LaunchedEffect(Unit) {
                // Atualizar o token assim que o usuário fizer login
                userToken = sessionManager.fetchAuthToken()
                if (!userToken.isNullOrEmpty()) {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true } //popUpTo para limpar a pilha de navegacao
                    }
                }
            }
        }
        composable("docDetail") { DocDetailActivity(modifier, navController, graphViewModel) }
        composable("taxDetail") { TaxDetailActivity(modifier, navController, graphViewModel) }

        navigation(startDestination = "dashboard", route = "main") {
            composable("dashboard") {
                BottomTabNavigation(modifier, navController, graphViewModel)
            }
        }
        navigation(startDestination = "taxes", route = "main") {
            composable("taxes") {
                BottomTabNavigation(modifier, navController, graphViewModel)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun BottomTabNavigation(modifier: Modifier = Modifier, mainHost: NavController, graphViewModel: GraphViewModel) {
    val navController = rememberNavController()

    val tabs = listOf(
        BottomNavItem("Histórico", "dashboard", R.drawable.ic_history),
        BottomNavItem("Gráfico", "taxes", R.drawable.ic_dashboard)
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
            composable("dashboard") { HistoryActivity(modifier, navController, mainHost, graphViewModel) }
            composable("taxes") { GraphActivity(modifier, navController, mainHost, graphViewModel) }
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
