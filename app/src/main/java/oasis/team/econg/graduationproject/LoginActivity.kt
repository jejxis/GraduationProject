package oasis.team.econg.graduationproject

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import oasis.team.econg.graduationproject.data.LoginDto
import oasis.team.econg.graduationproject.databinding.ActivityLoginBinding
import oasis.team.econg.graduationproject.retrofit.RetrofitManager
import oasis.team.econg.graduationproject.samplePreference.MyApplication
import oasis.team.econg.graduationproject.samplePreference.PreferenceUtil
import oasis.team.econg.graduationproject.utils.API
import oasis.team.econg.graduationproject.utils.Constants.TAG
import oasis.team.econg.graduationproject.utils.RESPONSE_STATE

class LoginActivity : AppCompatActivity() {
    val binding by lazy{ActivityLoginBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (ActivityCompat.checkSelfPermission(
                application,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                application,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                1
            )
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.loginEmail.text.toString().trim()
            val pw = binding.loginPw.text.toString().trim()

            if (email.isEmpty()/* || !Patterns.EMAIL_ADDRESS.matcher(email).matches()*/) {
                binding.loginEmail.error = "Check the Email"
                binding.loginEmail.requestFocus()
                return@setOnClickListener
            }
            if (pw.isEmpty()) {
                binding.loginPw.error = "Password required"
                binding.loginPw.requestFocus()
                return@setOnClickListener
            }

            val loginDto = LoginDto(email, pw)

            if(loginDto == null) return@setOnClickListener
            else{
                proceedLogin(loginDto)
            }

        }

        binding.btnSignup.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun proceedLogin(dto: LoginDto){
        Log.d(TAG, "in proceedLogin()")
        RetrofitManager.instance.signIn(dto, completion = {
            responseState, responseBody ->
            when(responseState){
                RESPONSE_STATE.OKAY -> {
                    MyApplication.prefs = PreferenceUtil(application)
                    MyApplication.prefs.token = responseBody
                    Log.d(TAG, MyApplication.prefs.token!!)
                    API.HEADER_TOKEN = "Bearer ${MyApplication.prefs.token}"
                    Log.d(TAG, "Login: api call success : $responseBody")
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK//액티비티 스택제거
                }
                RESPONSE_STATE.FAIL -> {
                    Log.d(TAG, "Login: api call fail : $responseBody")
                    Toast.makeText(this@LoginActivity, "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}