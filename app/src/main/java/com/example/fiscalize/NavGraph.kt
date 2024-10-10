package com.example.fiscalize

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.example.fiscalize.views.DashboardActivity
import com.example.fiscalize.views.HomeContent
import com.example.fiscalize.views.LoginActivity
import com.example.fiscalize.views.TaxesActivity
import com.example.fiscalize.ui.theme.mainRed
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val systemUiController = rememberSystemUiController()
    val navController = rememberNavController()

    systemUiController.setStatusBarColor(
        color = mainRed
    )

    NavHost( navController = navController, startDestination = "login", builder = {
        composable("home") { HomeContent(modifier,navController) }
        composable("dashboard") {  DashboardActivity(modifier,navController) }
        composable("login") { LoginActivity(modifier,navController) }
        composable("taxes") { TaxesActivity(modifier,navController) }

    })

}
