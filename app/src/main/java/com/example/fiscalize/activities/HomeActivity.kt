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
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.fiscalize.createImageFileUri
import com.example.fiscalize.uploadImageToApi


@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val context = LocalContext.current
    var capturedImageUri by remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            capturedImageUri?.let { uri ->
                uploadImageToApi(context, uri)
            }
        } else {
            Toast.makeText(context, "Falha ao capturar a imagem", Toast.LENGTH_SHORT).show()
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
            onClick = {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    capturedImageUri = createImageFileUri(context)
                    capturedImageUri?.let { uri -> cameraLauncher.launch(uri) }
                } else {
                    permissionLauncher.launch(
                        arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    )
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Text("+", color = Color.White, fontSize = 25.sp)
        }

        capturedImageUri?.let { uri ->
            Image(
                painter = rememberAsyncImagePainter(model = uri),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }
    }
}


