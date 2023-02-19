package oasis.team.econg.graduationproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import oasis.team.econg.graduationproject.data.PwDto
import oasis.team.econg.graduationproject.databinding.ActivityEditUserPwBinding
import oasis.team.econg.graduationproject.retrofit.RetrofitManager
import oasis.team.econg.graduationproject.utils.API
import oasis.team.econg.graduationproject.utils.Constants.TAG
import oasis.team.econg.graduationproject.utils.RESPONSE_STATE

class EditUserPwActivity : AppCompatActivity() {
    val binding by lazy{ActivityEditUserPwBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnProfileSave.setOnClickListener {
            if(checkPw()){
                proceedChangeUserPw()
            }
            else{
                Toast.makeText(this, "비밀번호를 확인하세요", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnClose.setOnClickListener {
            finish()
        }
    }
    private fun checkPw(): Boolean{
        val new = binding.editUserPw.text.toString()
        val check = binding.checkUserPw.text.toString()

        return check == new
    }

    private fun proceedChangeUserPw(){
        val pwDto = PwDto(binding.curPw.text.toString(), binding.editUserPw.text.toString())
        RetrofitManager.instance.changeUserPw(API.HEADER_TOKEN, pwDto, completion = {
            responseState ->
            when(responseState){
                RESPONSE_STATE.OKAY -> {
                    Log.d(TAG, "proceedChangeUserPw: api call success")
                    finish()
                }
                RESPONSE_STATE.FAIL -> {
                    Log.d(TAG, "proceedChangeUserPw: api call fail")
                    Toast.makeText(this@EditUserPwActivity,"비밀 번호 변경에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}