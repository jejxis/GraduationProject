package oasis.team.econg.graduationproject

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import oasis.team.econg.graduationproject.data.PlantsDetailResponseDto
import oasis.team.econg.graduationproject.databinding.ActivityMyPlantInfoBinding
import oasis.team.econg.graduationproject.retrofit.RetrofitManager
import oasis.team.econg.graduationproject.samplePreference.MyApplication
import oasis.team.econg.graduationproject.utils.API
import oasis.team.econg.graduationproject.utils.Constants.TAG
import oasis.team.econg.graduationproject.utils.CultureSettings
import oasis.team.econg.graduationproject.utils.RESPONSE_STATE
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import kotlin.math.floor

class MyPlantInfoActivity : AppCompatActivity() {
    val binding by lazy { ActivityMyPlantInfoBinding.inflate(layoutInflater) }
    var plantId: Long = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if(intent.hasExtra("plantId")){
            plantId = intent.getLongExtra("plantId", -1)
        }

        binding.btnClose.setOnClickListener {
            finish()
        }

        loadData()
    }

    private fun loadData(){
        RetrofitManager.instance.getPlantInfo(MyApplication.prefs.token, plantId, completion = {
            responseState, plantsDetailResponseDto ->
            when(responseState){
                RESPONSE_STATE.OKAY -> {
                    Log.d(TAG, "MyPlantInfoActivity getPlantInfo: SUCCESS")
                    val dto = plantsDetailResponseDto
                    if(dto != null) {
                        setData(dto)
                    }
                    else{
                        Toast.makeText(this@MyPlantInfoActivity, "데이터를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
                RESPONSE_STATE.FAIL -> {
                    Log.d(TAG, "MyPlantInfoActivity getPlantInfo: FAIL")
                    Toast.makeText(this@MyPlantInfoActivity, "데이터를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(this@MyPlantInfoActivity, "데이터를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun setData(dto: PlantsDetailResponseDto){
        var bitmap: Bitmap? = null
        val thread = object: Thread(){
            override fun run() {
                try{
                    var url = URL(dto.picture)
                    val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
                    conn.connect()
                    val inputStream = conn.inputStream
                    bitmap =  BitmapFactory.decodeStream(inputStream)
                }catch(e: IOException){
                    e.printStackTrace()
                }
            }
        }
        if(!dto.picture.isNullOrEmpty())
            thread.start()

        try{
            if(!dto.picture.isNullOrEmpty()){
                thread.join()
                binding.plantThumb.setImageBitmap(bitmap)
            }
            binding.plantName.text = dto.name
            binding.adoptionDate.text = dto.adoptingDate
            binding.waterInterval.text = "${dto.waterAlarmInterval}day(s)"
            binding.temperature.text = "${dto.lowTemperature}°C ~ ${dto.highTemperature}°C"
            binding.waterSupply.text = dto.waterSupply
            binding.light.text = CultureSettings.SUNSHINE_ARRAY[floor(dto.sunshine).toInt()]
        }catch (e: InterruptedException){
            e.printStackTrace()
        }
    }
}