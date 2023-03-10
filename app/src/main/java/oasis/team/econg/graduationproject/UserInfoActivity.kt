package oasis.team.econg.graduationproject

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import oasis.team.econg.graduationproject.data.UserDto
import oasis.team.econg.graduationproject.databinding.ActivityUserInfoBinding
import oasis.team.econg.graduationproject.retrofit.RetrofitManager
import oasis.team.econg.graduationproject.samplePreference.MyApplication
import oasis.team.econg.graduationproject.utils.API
import oasis.team.econg.graduationproject.utils.Constants
import oasis.team.econg.graduationproject.utils.RESPONSE_STATE
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class UserInfoActivity : AppCompatActivity() {
    val binding by lazy{ActivityUserInfoBinding.inflate(layoutInflater)}
    var user = UserDto("","","")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        loadData()

        binding.btnClose.setOnClickListener {
            finish()
        }

        binding.btnEditProfile.setOnClickListener {
            val intent = Intent(this@UserInfoActivity, EditUserInfoActivity::class.java)
            startActivity(intent)
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
            binding.email.setText(user.email)
            binding.editUserNickname.setText(user.nickName)
        }catch (e: InterruptedException){
            e.printStackTrace()
        }
    }

}