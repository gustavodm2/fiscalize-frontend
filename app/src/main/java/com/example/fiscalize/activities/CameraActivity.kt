package com.example.fiscalize.activities

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.fiscalize.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

import okhttp3.ResponseBody


@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun CameraContent(
    modifier: Modifier,
    uri: NavHostController? = null,
    onSetUri: (Uri) -> Unit = {}
) {
    val context = LocalContext.current
    val tempUri = remember { mutableStateOf<Uri?>(null) }
    val authority = stringResource(id = R.string.fileprovider)

    // Função para salvar a imagem na galeria
    @RequiresApi(Build.VERSION_CODES.Q)
    fun saveImageToGallery(context: Context, uri: Uri) {
        val contentResolver = context.contentResolver
        val sourceInputStream = contentResolver.openInputStream(uri)

        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "image_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        val imageUri: Uri? = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        imageUri?.let {
            contentResolver.openOutputStream(it)?.use { outputStream ->
                sourceInputStream?.copyTo(outputStream)
            }

            values.clear()
            values.put(MediaStore.Images.Media.IS_PENDING, 0)
            contentResolver.update(imageUri, values, null, null)

            Toast.makeText(context, "Image saved to Photos!", Toast.LENGTH_SHORT).show()
        } ?: run {
            Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show()
        }
    }

    // Função para salvar a imagem na galeria legada
    fun saveImageToLegacyGallery(context: Context, uri: Uri) {
        val picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val file = File(picturesDir, "image_${System.currentTimeMillis()}.jpg")

        val sourceInputStream = context.contentResolver.openInputStream(uri)

        try {
            val outputStream = FileOutputStream(file)
            sourceInputStream?.copyTo(outputStream)
            outputStream.flush()
            outputStream.close()

            MediaScannerConnection.scanFile(context, arrayOf(file.toString()), null, null)

            Toast.makeText(context, "Image saved to Photos!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show()
        }
    }

    // Função para fazer o upload da imagem
    fun uploadImage(context: Context, imageFile: File) {
        val requestFile = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)

        val apiService = RetrofitInstance.api
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.uploadImage(body)
                withContext(Dispatchers.Main) {
                    if (response.isExecuted) {
                        Toast.makeText(context, "Upload successful!", Toast.LENGTH_SHORT).show()
                        Log.d("bom", "foi legal")
                    } else {
                        Toast.makeText(context, "Upload failed: ", Toast.LENGTH_SHORT).show()
                        Log.d("ruim", "deu problema")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error byceasf: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Launcher para tirar foto
    val takePhotoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { isSuccess ->
            if (isSuccess) {
                tempUri.value?.let { uri ->
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        saveImageToGallery(context, uri)
                    } else {
                        saveImageToLegacyGallery(context, uri)
                    }
                    // Chama a função de upload
                    val imageFile = File(uri.path ?: "")
                    uploadImage(context, imageFile)
                    onSetUri.invoke(uri)
                }
            }
        }
    )

    // Launcher para permissão da câmera
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            val tmpUri = getTempUri(context, authority)
            tempUri.value = tmpUri
            takePhotoLauncher.launch(tempUri.value)
        } else {
            Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    var showBottomSheet by remember { mutableStateOf(false) }
    if (showBottomSheet) {
        MyModalBottomSheet(
            onDismiss = {
                showBottomSheet = false
            },
            onTakePhotoClick = {
                showBottomSheet = false

                val permission = Manifest.permission.CAMERA
                if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                    val tmpUri = getTempUri(context, authority)
                    if (tmpUri != null) {
                        tempUri.value = tmpUri
                        takePhotoLauncher.launch(tempUri.value)
                    } else {
                        Toast.makeText(context, "Failed to create file for photo", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    cameraPermissionLauncher.launch(permission)
                }
            },
            onPhotoGalleryClick = {
                showBottomSheet = false
            },
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {
                    showBottomSheet = true
                }
            ) {
                Text(text = "Select / Take")
            }
        }

        uri?.let {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = it,
                    modifier = Modifier.size(160.dp),
                    contentDescription = null,
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// Função para criar um URI temporário
fun getTempUri(context: Context, authority: String): Uri? {
    val storageDir = File(context.filesDir, "images")
    if (!storageDir.exists()) {
        val isDirCreated = storageDir.mkdirs()
        if (!isDirCreated) {
            Toast.makeText(context, "Failed to create directory for photo", Toast.LENGTH_SHORT).show()
            return null
        }
    }

    return try {
        val file = File.createTempFile(
            "image_" + System.currentTimeMillis().toString(),
            ".jpg",
            storageDir
        )

        FileProvider.getUriForFile(
            context,
            authority,
            file
        )
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Failed to create file for photo", Toast.LENGTH_SHORT).show()
        null
    }
}

// Componente para o BottomSheet
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyModalBottomSheet(
    onDismiss: () -> Unit,
    onTakePhotoClick: () -> Unit,
    onPhotoGalleryClick: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = { onDismiss() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Select an option",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )

            Button(onClick = { onTakePhotoClick() }) {
                Text(text = "Take Photo")
            }

            Button(onClick = { onPhotoGalleryClick() }) {
                Text(text = "Select from Gallery")
            }
        }
    }
}

data class BottomSheetItem(
    val title: String = "",
    val icon: ImageVector,
    val onClick: () -> Unit
)


