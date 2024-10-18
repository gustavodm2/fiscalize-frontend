package com.example.fiscalize.activities

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.example.fiscalize.R
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
    var capturedImageUri by remember { mutableStateOf<Uri?>(null) }
    var showDialog by remember { mutableStateOf(false) }

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
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Logo Image
                    Image(
                        painter = painterResource(R.drawable.logo),
                        contentDescription = "App Logo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.Gray, CircleShape)
                    )

                    Spacer(modifier = Modifier.width(16.dp))


                    Column(
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Razão Social: J&V",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = 16.sp,
                                fontStyle = FontStyle.Italic
                            ),
                            color = Color(0xFF333333)
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "CNPJ: 111.222.333-44",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 10.sp,
                                fontStyle = FontStyle.Italic
                            ),
                            color = Color(0xFF777777)
                        )
                    }
                }
            }
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
                    }) {
                        Text("Tirar Foto")
                    }
                },
                dismissButton = {
                    Button(onClick = {
                        showDialog = false
                        galleryLauncher.launch("image/*")
                    }) {
                        Text("Escolher da Galeria")
                    }
                }
            )
        }


    }
}


