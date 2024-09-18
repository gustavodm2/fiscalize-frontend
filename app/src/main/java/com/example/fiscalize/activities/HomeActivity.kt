package com.example.fiscalize.activities

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.fiscalize.ui.theme.FiscalizeTheme

    @Composable
    fun HomeContent(modifier: Modifier ,navController: NavHostController) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
           TextButton(onClick = { navController.navigate("dashboard")}) {
               Text(text = "Ir para Dashboard")
           }
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = { navController.navigate("camera")}) {
                Text(text = "Ir para Camera")
            }
        }
    }

