package com.example.fiscalize.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.fiscalize.R
import com.example.fiscalize.createImageFileUri
import com.example.fiscalize.ui.theme.appGray
import com.example.fiscalize.uploadImageToApi


@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun HomeContent(
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
                    .size(width = 380.dp, height = 100.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 8.dp),

                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ),
                border = BorderStroke(1.dp, Color.Black)
            ) {
                Row(
                    modifier = Modifier


                ) {
                    Image(
                        painter = painterResource(R.drawable.logo),
                        contentDescription = "App Logo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .border(border = BorderStroke(1.dp, Color.Black), shape = RoundedCornerShape(50.dp))
                            .size(88.dp)
                            .clip(CircleShape),
                        )
                    Column(
                        modifier = Modifier
                            .padding(start = 8.dp)
                    ){
                        val corporateReason = "J&V"
                        val CNPJ = "111.222.333-44"
                        Text(
                            text = "Razão Social: $corporateReason",
                            fontSize = 20.sp,
                            fontStyle = FontStyle.Italic,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = "CNPJ: $CNPJ",
                            fontSize = 20.sp,
                            fontStyle = FontStyle.Italic,
                            textAlign = TextAlign.Center
                        )
                    }
                }

            }
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
            onClick = { showDialog = true }, // Mostra o modal para escolher a ação
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


