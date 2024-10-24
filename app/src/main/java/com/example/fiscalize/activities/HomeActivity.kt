package com.example.fiscalize.activities

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.example.fiscalize.components.HeaderCard
import com.example.fiscalize.model.api.ApiService
import com.example.fiscalize.model.api.RetrofitInstance
import com.example.fiscalize.model.api.SessionManager
import com.example.fiscalize.model.user.User
import com.example.fiscalize.viewModel.createImageFileUri
import com.example.fiscalize.ui.theme.mainRed
import com.example.fiscalize.viewModel.uploadImageToApi


@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun HomeActivity(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val context = LocalContext.current
    val sessionManager: SessionManager = SessionManager(context)
    val apiClient: ApiService = RetrofitInstance.getApiService(context)
    var capturedImageUri by remember { mutableStateOf<Uri?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var userId by remember { mutableStateOf<String?>(null) }
    var user by remember { mutableStateOf<User?>(null) }

    LaunchedEffect(Unit) {
        userId = sessionManager.fetchUserId()
        userId?.let {
            Log.d("HomeActivity", "UserId obtido do SessionManager: $it")
            try {
                val response = apiClient.getUserData(it)
                if (response.isSuccessful) {
                    response.body()?.let { responseUser ->
                        user = responseUser
                        Log.d("HomeActivity", "Dados do usuário recebidos com sucesso: ${responseUser.corporateName}")
                    } ?: Log.e("HomeActivity", "Corpo da resposta está vazio.")
                } else {
                    Log.e("HomeActivity", "Falha ao obter dados do usuário: Código ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("HomeActivity", "Erro ao buscar dados do usuário: ${e.message}")
            }
        } ?: Log.e("HomeActivity", "userId é nulo.")
    }


    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            capturedImageUri?.let { uri ->
                uploadImageToApi(context, uri)
            }
        } else {
            Toast.makeText(context, "Falha ao capturar a imagem", Toast.LENGTH_SHORT).show()
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            capturedImageUri = it
            uploadImageToApi(context, it)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        if (permissions[Manifest.permission.CAMERA] == true) {
            capturedImageUri = createImageFileUri(context)
            capturedImageUri?.let { uri ->
                cameraLauncher.launch(uri)
            }
        } else {
            Toast.makeText(context, "Permissão de câmera negada", Toast.LENGTH_SHORT).show()
        }
    }

    val mainButtonColor = ButtonDefaults.buttonColors(
        containerColor = Color.DarkGray,
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
            HeaderCard(user = user)
            Spacer(Modifier.padding(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { navController.navigate("main")  },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = mainRed,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.elevatedButtonElevation(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                        .height(100.dp)
                ) {
                    Text(
                        text = "Simples Nacional",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }

                Button(
                    onClick = {  navController.navigate("dashboard")},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = mainRed,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.elevatedButtonElevation(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                        .height(100.dp)
                ) {
                    Text(
                        text = "Folha de pagamento",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(Modifier.padding(8.dp))



        }

        Button(
            colors = mainButtonColor,
            onClick = { showDialog = true }, 
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Text("+", color = Color.White, fontSize = 25.sp)
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(text = "Escolher ação") },
                text = { Text(text = "Deseja tirar uma foto ou escolher uma da galeria?") },
                confirmButton = {
                    Button(onClick = {
                        showDialog = false
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            capturedImageUri = createImageFileUri(context)
                            capturedImageUri?.let { uri -> cameraLauncher.launch(uri) }
                        } else {
                            permissionLauncher.launch(
                                arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            )
                        }
                    },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = mainRed,
                            contentColor = Color.White
                        ),) {
                        Text("Tirar Foto")
                    }
                },
                dismissButton = {
                    Button(onClick = {
                        showDialog = false
                        galleryLauncher.launch("image/*")
                    },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = mainRed,
                            contentColor = Color.White
                        ),) {
                        Text("Escolher da Galeria")
                    }
                }
            )
        }


    }
}


