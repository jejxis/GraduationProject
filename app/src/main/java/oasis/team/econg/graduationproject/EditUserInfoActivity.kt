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
import oasis.team.econg.graduationproject.samplePreference.MyApplication
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
    private var ischange = "false"
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
            ischange = "true"
        }
    }

    private fun loadData(){
        RetrofitManager.instance.getUser(MyApplication.prefs.token, completion = {
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
                else -> {
                    Toast.makeText(this, "사용자 정보를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
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
        val name = binding.editUserNickname.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        requestFile = pictureAdder.getRequestFile()
        if(requestFile != null){
            ischange = "true"
        }
        val change = ischange.toRequestBody("text/plain".toMediaTypeOrNull())
        RetrofitManager.instance.updateUserInfo(MyApplication.prefs.token, name, change, requestFile, completion = {responseState ->
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
                else -> {
                    Toast.makeText(this, "사용자 정보수정 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}