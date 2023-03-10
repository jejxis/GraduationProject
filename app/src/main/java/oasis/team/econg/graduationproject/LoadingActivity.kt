package oasis.team.econg.graduationproject

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import oasis.team.econg.graduationproject.databinding.ActivityLoadingBinding
import oasis.team.econg.graduationproject.retrofit.RetrofitManager
import oasis.team.econg.graduationproject.samplePreference.MyApplication
import oasis.team.econg.graduationproject.utils.Constants
import oasis.team.econg.graduationproject.utils.RESPONSE_STATE

class LoadingActivity : AppCompatActivity() {
    val binding by lazy{ActivityLoadingBinding.inflate(layoutInflater)}
    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if(checkPermission()){
            moveToAnotherActivity()
        }else{
            ActivityCompat.requestPermissions(this, permissions, 1)
        }

    }

    private fun checkPermission(): Boolean{
        for(permission in permissions){
            if(ActivityCompat.checkSelfPermission(
                    application,
                    permission
                ) != PackageManager.PERMISSION_GRANTED){
                return false
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(checkPermission()){
            moveToAnotherActivity()
        }
        else{
            Toast.makeText(this, "권한을 허용해야 사용이 가능합니다.", Toast.LENGTH_SHORT).show()
            exitProgram()
        }
    }

    private fun moveToAnotherActivity(){
        if(MyApplication.prefs != null && MyApplication.prefs.token != null){
            if(MyApplication.prefs.picture == null || MyApplication.prefs.nickname == null){
                loadUserData()
            }
            val intent = Intent(this@LoadingActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            val intent = Intent(this@LoadingActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun loadUserData(){
        RetrofitManager.instance.getUser(MyApplication.prefs.token, completion = {
                responseState, responseBody ->
            when(responseState){
                RESPONSE_STATE.OKAY -> {
                    Log.d(Constants.TAG, "EditUserInfoActivity - loadData(): api call success : $responseBody")
                    MyApplication.prefs.nickname = responseBody.nickName
                    MyApplication.prefs.picture = responseBody.picture
                    val intent = Intent(this@LoadingActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK//액티비티 스택제거
                }
                RESPONSE_STATE.FAIL -> {
                    Toast.makeText(this, "EditUserInfoActivity - loadData(): api call error", Toast.LENGTH_SHORT).show()
                    Log.d(Constants.TAG, "EditUserInfoActivity - loadData(): api call fail : $responseBody")
                }
                else -> {

                }
            }
        })
    }


    private fun exitProgram() {
        // 종료

        // 태스크를 백그라운드로 이동
        // moveTaskToBack(true);
        if (Build.VERSION.SDK_INT >= 21) {
            // 액티비티 종료 + 태스크 리스트에서 지우기
            finishAndRemoveTask()
        } else {
            // 액티비티 종료
            finish()
        }
        System.exit(0)
    }
}
