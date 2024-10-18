package com.example.fiscalize.activities

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.fiscalize.R
import com.example.fiscalize.ui.theme.mainRed
import com.example.fiscalize.ui.theme.offWhite


@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun LoginActivity(modifier: Modifier = Modifier, navController: NavHostController) {

    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current


    Box(
        Modifier.background(color = offWhite)
    ){
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center

        ) {

            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(250.dp)
            )
            OutlinedTextField(
                value = login,
                onValueChange = { login = it },
                label = { Text("Login") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Login Icon"
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFF7F2627),
                    cursorColor = Color(0xFF7F2627),
                    focusedLabelColor = Color(0xFF7F2627),
                    unfocusedLabelColor = Color(0xFF555555)
                ),
                modifier = Modifier
                    .width(350.dp)
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(10.dp),
            )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Senha") },

                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Password Icon"
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF7F2627),
                        cursorColor = Color(0xFF7F2627),
                        focusedLabelColor = Color(0xFF7F2627),
                        unfocusedLabelColor = Color(0xFF555555)
                    ),
                    modifier = Modifier
                        .width(350.dp)
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(10.dp),
                )

            Button(
                onClick = { Toast.makeText(context, "Login realizado com sucesso", Toast.LENGTH_SHORT).show();
                          navController.navigate("home")},
                modifier = Modifier.padding(6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = mainRed
                )
            ) {
                Text("Fazer login", color = Color.White)
            }
        }
    }
}