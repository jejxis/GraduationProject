package oasis.team.econg.graduationproject

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.JsonReader
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.net.toFile
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import oasis.team.econg.graduationproject.data.LoginDto
import oasis.team.econg.graduationproject.data.SignupDto
import oasis.team.econg.graduationproject.databinding.ActivitySignupBinding
import oasis.team.econg.graduationproject.retrofit.RetrofitManager
import oasis.team.econg.graduationproject.utils.Constants.TAG
import oasis.team.econg.graduationproject.utils.RESPONSE_STATE
import oasis.team.econg.graduationproject.utils.asMultipart
import oasis.team.econg.graduationproject.utils.toMultipartBody
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
//https://velog.io/@dev_thk28/Android-Retrofit2-Multipart%EC%82%AC%EC%9A%A9%ED%95%98%EA%B8%B0-Java
class SignupActivity : AppCompatActivity() {
    val binding by lazy{ActivitySignupBinding.inflate(layoutInflater)}
    private lateinit var pictureAdder: PictureAdder
    private var requestFile: MultipartBody.Part? = null
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        pictureAdder = PictureAdder(this, binding.signupProfile)

        binding.siggnupChangeProfile.setOnClickListener{
            pictureAdder.getImage()
        }

        binding.btnSignUp.setOnClickListener {
            proceedSignUp()
        }

    }

    private fun proceedSignUp(){
        val login = SignupDto(binding.signupEmail.text.toString(),
            binding.signupPw.text.toString(),
            binding.signupNickname.text.toString()
        )
        val key = Gson().toJson(login).toString().toRequestBody("text/plain".toMediaTypeOrNull())
        //val key = GsonBuilder().setLenient().create().toJson(login).toString().toRequestBody("text/plain".toMediaTypeOrNull())
        //Log.d(TAG, "proceedSignUp - key: $key")
        requestFile = pictureAdder.getRequestFile()

        RetrofitManager.instance.signUp(key, requestFile, completion = {responseState ->
            when(responseState){
                RESPONSE_STATE.OKAY -> {
                    Log.d(TAG, "Signup SUCCESS")
                    val intent = Intent(this@SignupActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                RESPONSE_STATE.FAIL -> {
                    Log.d(TAG, "Signup FAIL")
                }
            }
        })
    }

}