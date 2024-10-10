package com.example.fiscalize.views

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController


@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun DashboardActivity(modifier: Modifier , navController: NavHostController) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Text(text = "Hello Dashboard")
    }
}