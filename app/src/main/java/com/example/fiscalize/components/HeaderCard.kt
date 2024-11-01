package com.example.fiscalize.components

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.fiscalize.R
import com.example.fiscalize.model.api.SessionManager
import com.example.fiscalize.model.user.User



@Composable
fun HeaderCard(
    user: User?,
    navController: NavHostController,
    context: Context
) {
    val sessionManager = SessionManager(context)

    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(120.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        border = BorderStroke(1.dp, Color.LightGray),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxSize()
            ) {
                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    verticalArrangement = Arrangement.Center
                ) {
                    if (user != null) {
                        Text(
                            text = user.corporateName,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = 16.sp,
                                fontStyle = FontStyle.Italic
                            ),
                            color = Color(0xFF333333)
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = user.cnpj,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 10.sp,
                                fontStyle = FontStyle.Italic
                            ),
                            color = Color(0xFF777777)
                        )
                    } else {
                        Text(
                            text = "Carregando informações do usuário...",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = 16.sp,
                                fontStyle = FontStyle.Italic
                            ),
                            color = Color(0xFF777777)
                        )
                    }
                }
            }

            Icon(
                painter = painterResource(id = R.drawable.baseline_logout_24),
                contentDescription = "User Icon",
                tint = Color.Gray,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(40.dp)
                    .padding(8.dp)
                    .clickable {
                        sessionManager.clearAuthToken()
                        navController.navigate("login")
                    }
            )
        }
    }
}

