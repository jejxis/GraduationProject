package oasis.team.econg.graduationproject.utils

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.OpenableColumns
import androidx.annotation.RequiresApi
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.BufferedSink
import okio.source
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


fun String?.isJsonObject():Boolean {
    if(this == null) return false
    else return this!!.startsWith("{") == true && this!!.endsWith("}")
}

fun String?.isJsonArray():Boolean {
    if(this == null) return false
    else return this!!.startsWith("[") == true && this!!.endsWith("]")
}

@SuppressLint("Range")
fun Uri.asMultipart(name: String, contentResolver: ContentResolver): MultipartBody.Part?? {
    return contentResolver.query(this, null, null, null, null)?.let {
        if (it.moveToNext()) {
            val displayName = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME));
            val requestBody = object : RequestBody() {
                override fun contentType(): MediaType? {
                    return contentResolver.getType(this@asMultipart)?.toMediaType()
                }

                override fun writeTo(sink: BufferedSink) {
                    sink.writeAll(contentResolver.openInputStream(this@asMultipart)?.source()!!)
                }
            }
            it.close()
            MultipartBody.Part.createFormData(name, displayName, requestBody)
        } else {
            it.close()
            null
        }
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
fun Uri.toMultipartBody(context: Context): MultipartBody.Part {
    //https://black-jin0427.tistory.com/179
    //https://bubblebubble.tistory.com/15
    /*val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, this)
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
    return byteArrayOutputStream.toByteArray()*/
    val contentResolver = context.contentResolver
    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, this)
    val dir = File(context.cacheDir.path)
    val file = File(dir, "profile.jpg")

    try {
        file.createNewFile()
        val out = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, out)
        out.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
    val requestBody: RequestBody =
        RequestBody.create("image/jpg".toMediaTypeOrNull(), file)
    val uploadFile = MultipartBody.Part.createFormData("file", file.name, requestBody)

    return uploadFile
}
