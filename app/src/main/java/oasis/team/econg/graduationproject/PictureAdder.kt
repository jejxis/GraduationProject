package oasis.team.econg.graduationproject

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import oasis.team.econg.graduationproject.utils.Constants.TAG
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


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
            imgView.setImageURI(imagePath)

            val file = File(absolutelyPath(imagePath, app))
            val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            requestFile = MultipartBody.Part.createFormData("file", file.name, requestBody)

            Log.d(TAG,file.name)
        }
    }
}