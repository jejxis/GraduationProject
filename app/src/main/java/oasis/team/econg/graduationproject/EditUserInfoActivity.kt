package oasis.team.econg.graduationproject

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import oasis.team.econg.graduationproject.data.UserDto
import oasis.team.econg.graduationproject.databinding.ActivityEditUserInfoBinding
import oasis.team.econg.graduationproject.retrofit.RetrofitManager
import oasis.team.econg.graduationproject.utils.API
import oasis.team.econg.graduationproject.utils.Constants
import oasis.team.econg.graduationproject.utils.Constants.TAG
import oasis.team.econg.graduationproject.utils.RESPONSE_STATE
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.IOException
import java.lang.Package.getPackage
import java.net.HttpURLConnection
import java.net.URL

class EditUserInfoActivity : AppCompatActivity() {
    val binding by lazy{ActivityEditUserInfoBinding.inflate(layoutInflater)}
    private lateinit var pictureAdder: PictureAdder
    private var user = UserDto("","","")
    private var requestFile: MultipartBody.Part? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        loadData()

        pictureAdder = PictureAdder(this, binding.userProfile)

        binding.userProfile.setOnClickListener {
            pictureAdder.getImage()
        }

        binding.btnProfileSave.setOnClickListener {
            if(binding.editUserNickname.text.toString().isNullOrEmpty()) return@setOnClickListener
            proceedUpdateUserInfo()
        }

        binding.btnClose.setOnClickListener {
            finish()
        }

        binding.btnProfileCancel.setOnClickListener {
            finish()
        }

        binding.btnSetBasic.setOnClickListener {
            binding.userProfile.setImageResource(R.drawable.flower_pot)
            val path = Uri.parse("android.resource://"+ packageName +"/"+R.drawable.flower_pot).toString()
            val file = File(path)
            Log.d(TAG, "onCreate: $file")
            val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            requestFile = MultipartBody.Part.createFormData("file", file.name, requestBody)
        }
    }

    private fun loadData(){
        RetrofitManager.instance.getUser(API.HEADER_TOKEN, completion = {
            responseState, responseBody ->
            when(responseState){
                RESPONSE_STATE.OKAY -> {
                    Log.d(Constants.TAG, "EditUserInfoActivity - loadData(): api call success : $responseBody")
                    user = responseBody
                    setData()
                }
                RESPONSE_STATE.FAIL -> {
                    Toast.makeText(this, "EditUserInfoActivity - loadData(): api call error", Toast.LENGTH_SHORT).show()
                    Log.d(Constants.TAG, "EditUserInfoActivity - loadData(): api call fail : $responseBody")
                }
            }
        })
    }

    private fun setData(){
        var bitmap: Bitmap? = null
        val thread = object: Thread(){
            override fun run() {
                try{
                    var url = URL(user.picture)
                    val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
                    conn.connect()
                    val inputStream = conn.inputStream
                    bitmap =  BitmapFactory.decodeStream(inputStream)
                }catch(e: IOException){
                    e.printStackTrace()
                }
            }
        }
        if(!user.picture.isNullOrEmpty())
            thread.start()
        else
            binding.userProfile.setImageResource(R.drawable.flower_pot)

        try{
            if(!user.picture.isNullOrEmpty()){
                thread.join()
                binding.userProfile.setImageBitmap(bitmap)
            }
            binding.email.text = user.email
            binding.editUserNickname.setText(user.nickName)
        }catch (e: InterruptedException){
            e.printStackTrace()
        }
    }

    private fun proceedUpdateUserInfo(){
        val key = binding.editUserNickname.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        if(requestFile == null)
            requestFile = pictureAdder.getRequestFile()

        RetrofitManager.instance.updateUserInfo(API.HEADER_TOKEN, key, requestFile, completion = {responseState ->
            when(responseState){
                RESPONSE_STATE.OKAY -> {
                    Log.d(TAG, "proceedUpdateUserInfo: SUCCESS")
                    val intent = Intent(this@EditUserInfoActivity, UserInfoActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                RESPONSE_STATE.FAIL -> {
                    Log.d(TAG, "proceedUpdateUserInfo: FAIL")
                }
            }
        })
    }
}