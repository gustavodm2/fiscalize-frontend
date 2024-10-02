package com.example.fiscalize.activities

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController


@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun LoginActivity(modifier: Modifier , navController: NavHostController) {

    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        OutlinedTextField(
            value = login,
            onValueChange = {login = it},
            label = { Text("Login")},
            modifier = Modifier.padding(6.dp)

        )

        OutlinedTextField(
            value = password,
            onValueChange = {password = it},
            label = { Text("Senha")}
        )

        Button(
            onClick = { Toast.makeText(context, "Logando usuario", Toast.LENGTH_SHORT).show() } ,
            modifier = Modifier.padding(6.dp),
        ) {
            Text("Fazer login", color = Color.White)
        }

    }
}