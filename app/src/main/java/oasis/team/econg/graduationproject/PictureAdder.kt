package oasis.team.econg.graduationproject

import android.Manifest
import android.net.Uri
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class PictureAdder(app: AppCompatActivity, imgView: ImageView) {
    var filePath = ""
    private var imgUri: Uri? = null
    var myapp = app
    private val galleryLauncher = app.registerForActivityResult(ActivityResultContracts.GetContent()){
            uri ->
        filePath = makeFilePath("images", "temp", uri!!)
        imgUri = uri
        imgView.setImageURI(uri)
    }

    private val permissionLauncher = app.registerForActivityResult(ActivityResultContracts.RequestPermission()){ isGranted ->
        if(isGranted){
            galleryLauncher.launch("image/*")
        }else{
            Toast.makeText(app.baseContext, "외부 저장소 읽기 권한을 승인해야 사용할 수 있습니다", Toast.LENGTH_SHORT).show()
        }
    }

    private fun makeFilePath(path: String, userId: String, uri: Uri): String{
        filePath = ""
        val mimeType = myapp.contentResolver.getType(uri)?:"/none"
        val ext = mimeType.split("/")[1]
        val timeSuffix = System.currentTimeMillis()
        val filename = "/${path}/${userId}_${timeSuffix}.${ext}"

        return filename
    }

    fun launch(){
        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }
}