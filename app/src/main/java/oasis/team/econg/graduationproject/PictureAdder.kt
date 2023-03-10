package oasis.team.econg.graduationproject

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import oasis.team.econg.graduationproject.utils.Constants.TAG
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStream


class PictureAdder(app: AppCompatActivity, imgView: ImageView) {
    companion object {
        const val REQ_SELECT_IMG = 200
    }
    private var requestFile: MultipartBody.Part? = null

    fun getRequestFile(): MultipartBody.Part?{
        return requestFile
    }

    fun getImage(){

        Log.d(TAG,"사진변경 호출")
        val chooserIntent = Intent(Intent.ACTION_CHOOSER)
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        chooserIntent.putExtra(Intent.EXTRA_INTENT, intent)
        chooserIntent.putExtra(Intent.EXTRA_TITLE,"사용할 앱을 선택해주세요.")
        launcher.launch(chooserIntent)
    }
    fun absolutelyPath(path: Uri?, context : Context): String {
        var proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        var c: Cursor? = context.contentResolver.query(path!!, proj, null, null, null)
        var index = c?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c?.moveToFirst()

        var result = c?.getString(index!!)

        return result!!
    }
    var launcher = app.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val imagePath = result.data!!.data

            val file = File(absolutelyPath(imagePath, app))
            if(file.length() > 10 * 1024 * 1024){
                Toast.makeText(app, "사진 용량이 10MB를 초과하였습니다. 다른 사진을 골라주세요.", Toast.LENGTH_SHORT).show()
                return@registerForActivityResult
            }

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                compressImage(app, imagePath!!, file)
            }else{
                val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), file)
                requestFile = MultipartBody.Part.createFormData("file", file.name, requestBody)
            }

            imgView.setImageURI(imagePath)
            Log.d(TAG,file.name)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun compressImage(app: AppCompatActivity, uri: Uri, file: File){
        lateinit var inputStream: InputStream
        try{
            inputStream = app.contentResolver.openInputStream(uri)!!
        }catch (e: IOException){
            e.printStackTrace()
        }
        var bitmap: Bitmap? = BitmapFactory.decodeStream(inputStream)
        val degree = fileToDegrees(file)
        bitmap = rotate(bitmap, degree)
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap!!.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream)
        val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), byteArrayOutputStream.toByteArray())
        requestFile = MultipartBody.Part.createFormData("file", file.name, requestBody)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun fileToDegrees(file: File): Int {
        val exif = ExifInterface(file)
        val exifOrientation: Int = exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270
        }
        return 0
    }

    fun rotate(bitmap: Bitmap?, degrees: Int): Bitmap? { // 이미지 회전 및 이미지 사이즈 압축
        var bitmap = bitmap
        if (degrees != 0 && bitmap != null) {
            val m = Matrix()
            m.setRotate(degrees.toFloat(), bitmap.width.toFloat() / 2,
                bitmap.height.toFloat() / 2)
            try {
                val converted = Bitmap.createBitmap(bitmap, 0, 0,
                    bitmap.width, bitmap.height, m, true)
                if (bitmap != converted) {
                    bitmap.recycle()
                    bitmap = converted
                    val options = BitmapFactory.Options()
                    options.inSampleSize = 4
                    bitmap = Bitmap.createScaledBitmap(bitmap, 1280, 1280, true) // 이미지 사이즈 줄이기
                }
            } catch (ex: OutOfMemoryError) {
                // 메모리가 부족하여 회전을 시키지 못할 경우 그냥 원본을 반환합니다.
            }
        }
        return bitmap
    }

}