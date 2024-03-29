package oasis.team.econg.graduationproject

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
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
    private var email = ""
    private var nickname = ""
    private var pw = ""
    //@RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        pictureAdder = PictureAdder(this, binding.signupProfile)

        binding.siggnupChangeProfile.setOnClickListener{
            pictureAdder.getImage()
        }

        binding.btnSignUp.setOnClickListener {
            email = binding.signupEmail.text.toString().trim()
            pw = binding.signupPw.text.toString().trim()
            nickname = binding.signupNickname.toString().trim()

            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.signupEmail.error = "Check the Email"
                binding.signupEmail.requestFocus()
                return@setOnClickListener
            }
            if (pw.isEmpty()) {
                binding.signupPw.error = "Password required"
                binding.signupPw.requestFocus()
                return@setOnClickListener
            }
            if(nickname.isEmpty()){
                binding.signupNickname.error = "Nickname required"
                binding.signupNickname.requestFocus()
                return@setOnClickListener
            }
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
                RESPONSE_STATE.NOT_FOUND -> {
                    Toast.makeText(this, "페이지를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
                }
                RESPONSE_STATE.EXIST_USER -> {
                    Toast.makeText(this, "이미 존재하는 사용자입니다.", Toast.LENGTH_SHORT).show()
                }
                RESPONSE_STATE.FAIL -> {
                    Log.d(TAG, "Signup FAIL")
                }
                else -> {
                    Toast.makeText(this, "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

}