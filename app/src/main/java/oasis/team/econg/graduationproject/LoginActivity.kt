package oasis.team.econg.graduationproject

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import oasis.team.econg.graduationproject.data.LoginDto
import oasis.team.econg.graduationproject.databinding.ActivityLoginBinding
import oasis.team.econg.graduationproject.retrofit.RetrofitManager
import oasis.team.econg.graduationproject.samplePreference.MyApplication
import oasis.team.econg.graduationproject.samplePreference.PreferenceUtil
import oasis.team.econg.graduationproject.utils.API
import oasis.team.econg.graduationproject.utils.Constants.TAG
import oasis.team.econg.graduationproject.utils.RESPONSE_STATE
import java.util.logging.Logger

class LoginActivity : AppCompatActivity() {
    val binding by lazy{ActivityLoginBinding.inflate(layoutInflater)}
    var fcmToken = ""
    @RequiresApi(33)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        createNotificationChannel("notificationPermission", "notification")
        if (ActivityCompat.checkSelfPermission(
                application,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                application,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                application,
                Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.POST_NOTIFICATIONS),
                1
            )
        }

        pushToken()
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
            val loginDto = makeLoginDto(email, pw)

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
        Log.d(TAG, "proceedLogin() - dto: $dto")
        RetrofitManager.instance.signIn(dto, completion = {
            responseState, responseBody ->
            when(responseState){
                RESPONSE_STATE.OKAY -> {
                    MyApplication.prefs = PreferenceUtil(application)
                    MyApplication.prefs.token = responseBody
                    Log.d(TAG, MyApplication.prefs.token!!)
                    API.HEADER_TOKEN = "Bearer ${MyApplication.prefs.token}"
                    Log.d(TAG, "Login: api call success : $responseBody")
                    applyTopic()
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

    private fun applyTopic(){
        FirebaseMessaging.getInstance().subscribeToTopic("weather-notification").addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(TAG,"구독 요청 성공")
            } else {
                Log.d(TAG, "구독 요청 실패")
            }
        }
    }

    private fun pushToken(){
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task: Task<String> ->
                if (!task.isSuccessful) {
                    Log.d(TAG, "pushToken: Fetching FCM registration token failed" + task.exception)
                    return@addOnCompleteListener
                }
                fcmToken = task.result
                Log.d(TAG, "pushToken: $fcmToken")
                return@addOnCompleteListener
        }
    }

    private fun makeLoginDto(email: String, pw: String): LoginDto{
        Log.d(TAG, "in makeLoginDto.. is it after pushToken?")
        return LoginDto(email, pw, fcmToken)
    }

    private fun createNotificationChannel(channelId: String, channelName: String){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT))
        }
    }
}