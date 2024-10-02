package com.example.fiscalize.activities

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Surface
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
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.fiscalize.R
import com.example.fiscalize.ui.theme.FiscalizeTheme
import java.io.File
import java.io.FileOutputStream


@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun CameraContent(
    modifier: Modifier,
    navController: NavController,
    uri: Uri? = null,
    directory: File? = null,
    onSetUri: (Uri) -> Unit = {}
) {
    val context = LocalContext.current
    val tempUri = remember { mutableStateOf<Uri?>(null) }
    val authority = stringResource(id = R.string.fileprovider)

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
                    onSetUri.invoke(uri)
                }
            }
        }
    )

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
    if (showBottomSheet){
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

    Column (
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
        Text(text = "Ir para home")



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


@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun MyModalBottomSheet(
    onDismiss: () -> Unit,
    onTakePhotoClick: () -> Unit,
    onPhotoGalleryClick: () -> Unit
) {
    MyModalBottomSheetContent(
        header = "Choose Option",
        onDismiss = {
            onDismiss.invoke()
        },
        items = listOf(
            BottomSheetItem(
                title = "Take Photo",
                icon = Icons.Default.AccountBox,
                onClick = {
                    onTakePhotoClick.invoke()
                }
            ),
            BottomSheetItem(
                title = "select image",
                icon = Icons.Default.Place,
                onClick = {
                    onPhotoGalleryClick.invoke()
                }
            ),
        )
    )
}


@RequiresApi(Build.VERSION_CODES.N)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyModalBottomSheetContent(
    onDismiss: () -> Unit,
    header: String = "Choose Option",

    items: List<BottomSheetItem> = listOf(),
) {
    val skipPartiallyExpanded by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded
    )
    val edgeToEdgeEnabled by remember { mutableStateOf(false) }
    val windowInsets = if (edgeToEdgeEnabled)
        WindowInsets(0) else BottomSheetDefaults.windowInsets

    ModalBottomSheet(
        shape = MaterialTheme.shapes.medium.copy(
            bottomStart = CornerSize(0),
            bottomEnd = CornerSize(0)
        ),
        onDismissRequest = { onDismiss.invoke() },
        sheetState = bottomSheetState,
        windowInsets = windowInsets
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                text = header,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
            items.forEach {item ->
                androidx.compose.material3.ListItem(
                    modifier = Modifier.clickable {
                        item.onClick.invoke()
                    },
                    headlineContent = {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.titleMedium,
                        )
                    },
                    leadingContent = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title
                        )
                    },
                )
            }
        }
    }
}

data class BottomSheetItem(
    val title: String = "",
    val icon: ImageVector,
    val onClick: () -> Unit
)


