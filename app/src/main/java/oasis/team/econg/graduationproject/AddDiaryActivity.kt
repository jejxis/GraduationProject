package oasis.team.econg.graduationproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.gson.JsonObject
import oasis.team.econg.graduationproject.databinding.ActivityAddDiaryBinding
import oasis.team.econg.graduationproject.retrofit.RetrofitManager
import oasis.team.econg.graduationproject.utils.API
import oasis.team.econg.graduationproject.utils.Constants.TAG
import oasis.team.econg.graduationproject.utils.RESPONSE_STATE
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class AddDiaryActivity : AppCompatActivity() {
    val binding by lazy{ActivityAddDiaryBinding.inflate(layoutInflater)}
    var id: Long = -1
    private var requestFile: MultipartBody.Part? = null
    private lateinit var pictureAdder: PictureAdder
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        pictureAdder = PictureAdder(this, binding.plantImage)
        if(intent.hasExtra("id")){
            id = intent.getLongExtra("id", -1)
        }

        binding.btnClose.setOnClickListener {
            finish()
        }

        binding.addPicture.setOnClickListener {
            pictureAdder.getImage()
        }

        binding.btnAddDiary.setOnClickListener {
            uploadDiary()
        }
    }

    private fun uploadDiary() {
        requestFile = pictureAdder.getRequestFile()
        val jsonObject = JSONObject()
        jsonObject.put("content", binding.diaryContext.text.toString())
        val key = jsonObject.toString().toRequestBody("text/plain".toMediaTypeOrNull())

        RetrofitManager.instance.postJournals(
            auth = API.HEADER_TOKEN, plantId = id, key = key, file = requestFile,completion = {
                responseState, responseBody ->
                when(responseState){
                    RESPONSE_STATE.OKAY ->{
                        Log.d(TAG, "AddDiaryActivity: uploadDiary(): result: $responseBody")
                        val intent = Intent(this@AddDiaryActivity, DiaryListActivity::class.java)
                        intent.putExtra("id", id)
                        startActivity(intent)
                        finish()
                    }
                    RESPONSE_STATE.FAIL ->{
                        Log.d(TAG, "AddDiaryActivity: uploadDiary(): api call fail: $responseBody")
                        Toast.makeText(this@AddDiaryActivity, "다이어리 작성에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            })
    }
}