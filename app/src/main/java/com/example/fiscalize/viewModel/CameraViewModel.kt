package com.example.fiscalize.viewModel

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.content.FileProvider
import com.example.fiscalize.BuildConfig
import com.example.fiscalize.model.api.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@SuppressLint("SimpleDateFormat")
fun createImageFileUri(context: Context): Uri? {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageFileName = "PNG_" + timeStamp + "_"
    val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    if (storageDir != null && !storageDir.exists()) {
        storageDir.mkdirs()
    }
    val imageFile = File.createTempFile(imageFileName, ".png", storageDir)
    return FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", imageFile)
}

fun compressImage(inputStream: InputStream, maxSizeKB: Int): ByteArray {
    val bitmap = BitmapFactory.decodeStream(inputStream)
    val outputStream = ByteArrayOutputStream()

    var quality = 100
    do {
        outputStream.reset()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        quality -= 10
    } while (outputStream.size() / 1024 > maxSizeKB && quality > 0)

    return outputStream.toByteArray()
}


fun uploadImageToApi(context: Context, imageUri: Uri) {
    val contentResolver = context.contentResolver
    val inputStream = contentResolver.openInputStream(imageUri)
    val compressedImageBytes = compressImage(inputStream!!, 500)

    val maxBytes = 5 * 1024 * 1024

    val buffer = ByteArrayOutputStream()

    try {
        val byteArray = ByteArray(1024)
        var totalBytesRead = 0
        var len: Int

        while (inputStream.read(byteArray).also { len = it } != -1 && totalBytesRead < maxBytes) {
            if (totalBytesRead + len > maxBytes) {
                len = maxBytes - totalBytesRead
            }

            buffer.write(byteArray, 0, len)
            totalBytesRead += len
        }



        val requestBody = compressedImageBytes.toRequestBody("image/jpeg".toMediaTypeOrNull())
        val timestamp = System.currentTimeMillis().toString()
        val multipartBody = MultipartBody.Part.createFormData(
            "file",
            "$timestamp.png",
            requestBody
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.uploadImage(multipartBody)

                if (response.isSuccessful) {
                    Log.d("UPLOAD", "Imagem enviada com sucesso: ${response}")

                } else {
                    Log.e("UPLOAD", "Erro no envio da imagem: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("UPLOAD", "Falha na comunicação: ${e.message}")
            }
        }
    } catch (e: Exception) {
        Log.e("UPLOAD", "Erro ao processar imagem: ${e.message}")
    } finally {
        inputStream.close()
        buffer.close()
    }
}