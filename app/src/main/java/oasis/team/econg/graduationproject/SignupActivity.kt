package oasis.team.econg.graduationproject

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import oasis.team.econg.graduationproject.data.SignupDto
import oasis.team.econg.graduationproject.databinding.ActivitySignupBinding
import oasis.team.econg.graduationproject.retrofit.RetrofitManager
import oasis.team.econg.graduationproject.utils.Constants.TAG
import oasis.team.econg.graduationproject.utils.RESPONSE_STATE
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
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