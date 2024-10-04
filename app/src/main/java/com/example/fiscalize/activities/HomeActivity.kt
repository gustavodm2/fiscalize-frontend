package com.example.fiscalize.activities

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun HomeContent(modifier: Modifier = Modifier, navController: NavHostController) {

    val mainButtonColor = ButtonDefaults.buttonColors(
        containerColor = androidx.compose.ui.graphics.Color.DarkGray,
        contentColor = MaterialTheme.colorScheme.surface
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = "Bem vindo, Roberto", fontSize = 20.sp, fontStyle = FontStyle.Italic)
            Spacer(Modifier.padding(8.dp))


            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f)
                    .border(2.dp, Color.Black, RoundedCornerShape(10.dp))
                    .padding(8.dp)
            ) {
                items(listOf("ICMS", "CFOP", "NCM")) { item ->
                    Box(
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .padding(4.dp)
                            .border(1.dp, Color.Black)
                            .padding(8.dp)
                    ) {
                        Text(text = item, fontSize = 20.sp)
                    }
                }
            }
        }

        Button(
            colors = mainButtonColor,
            onClick = { navController.navigate("camera") },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Text("+", color = Color.White, fontSize = 25.sp)
        }
    }
}

