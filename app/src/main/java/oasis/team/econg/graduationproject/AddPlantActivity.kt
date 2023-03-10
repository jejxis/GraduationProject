package oasis.team.econg.graduationproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.gson.Gson
import oasis.team.econg.graduationproject.data.DetailToMineDto
import oasis.team.econg.graduationproject.data.PlantsPostDto
import oasis.team.econg.graduationproject.databinding.ActivityAddPlantBinding
import oasis.team.econg.graduationproject.dialog.CultureSettingFragment
import oasis.team.econg.graduationproject.dialog.DatePickerFragment
import oasis.team.econg.graduationproject.dialog.TemperatureSettingFragment
import oasis.team.econg.graduationproject.retrofit.RetrofitManager
import oasis.team.econg.graduationproject.rvAdapter.MyPlantAdapter
import oasis.team.econg.graduationproject.samplePreference.MyApplication
import oasis.team.econg.graduationproject.utils.API
import oasis.team.econg.graduationproject.utils.Constants
import oasis.team.econg.graduationproject.utils.Constants.TAG
import oasis.team.econg.graduationproject.utils.CultureSettings
import oasis.team.econg.graduationproject.utils.RESPONSE_STATE
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import kotlin.math.*

class AddPlantActivity : AppCompatActivity() {
    val binding by lazy{ActivityAddPlantBinding.inflate(layoutInflater)}
    var requestFile : MultipartBody.Part? = null
    var waterAlarmInterval = 0
    var temStart = 0
    var temEnd = 0
    private lateinit var pictureAdder: PictureAdder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        if(intent.hasExtra("Mine")){
            var mineDto: DetailToMineDto = intent.getSerializableExtra("Mine") as DetailToMineDto
            setScreen(mineDto)
        }
        pictureAdder = PictureAdder(this, binding.plantThumb)
        binding.ratingBarSunshine.setOnRatingBarChangeListener {
                ratingBar, fl, b
            ->  binding.sunshine.text = fl.toInt().toString()
        }

        binding.ratingBarWater.setOnRatingBarChangeListener {
                ratingBar, fl, b
            ->  binding.waterAmount.text = CultureSettings.WATER_ARRAY[floor(fl.toDouble()).toInt()]
        }

        binding.plantThumb.setOnClickListener {
            pictureAdder.getImage()
        }

        binding.adoptionDate.setOnClickListener {
            Log.d("ADDPLANT", "click")
            val newFragment: DialogFragment = DatePickerFragment()
            newFragment.show(supportFragmentManager, "datePicker")
        }

        binding.btnCultureSetting.setOnClickListener {
            val cultureSettingFragment: CultureSettingFragment = CultureSettingFragment()
            cultureSettingFragment.show(supportFragmentManager,"cultureSetting")
        }

        binding.btnTemperatureSetting.setOnClickListener {
            val temperatureSettingFragment: TemperatureSettingFragment = TemperatureSettingFragment()
            temperatureSettingFragment.show(supportFragmentManager, "temperatureSetting")
        }

        binding.btnClose.setOnClickListener {
            finish()
        }

        binding.btnSave.setOnClickListener {
            val dto = makePlant()
            requestFile = pictureAdder.getRequestFile()
            if(dto == null || requestFile == null) return@setOnClickListener
            else{
                val key = Gson().toJson(dto).toString().toRequestBody("text/plain".toMediaTypeOrNull())
                RetrofitManager.instance.postPlants(auth = MyApplication.prefs.token, key = key, file = requestFile, completion = {
                    responseState, responseBody ->
                    when(responseState){
                        RESPONSE_STATE.OKAY ->{
                            Log.d(TAG, "AddPlantActivity - postPlants() result: $responseBody")
                            val intent = Intent(this@AddPlantActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        RESPONSE_STATE.FAIL ->{
                            Log.d(TAG, "AddPlantActivity - postPlants() call fail: $responseBody")
                            Toast.makeText(this@AddPlantActivity, "결제 요청에 실패했습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }
        }
    }

    fun makePlant(): PlantsPostDto?{
        return PlantsPostDto(
            name = binding.editPlantName.text.toString(),
            waterAlarmInterval = waterAlarmInterval,
            waterSupply = binding.waterAmount.text.toString(),
            sunshine = binding.sunshine.text.toString().toDouble(),
            highTemperature = temEnd,
            lowTemperature = temStart
        )
    }

    fun processDatePickerResult(year: Int, month: Int, day: Int) {
        var month_string = Integer.toString(month + 1)
        var day_string = Integer.toString(day)
        val year_string = Integer.toString(year)
        if(month < 9) month_string = "0$month_string"
        if(day < 10) day_string = "0$day_string"
        val dateMessage = "$year_string-$month_string-$day_string"
        binding.adoptionDate.text = dateMessage
    }

    fun setCultureSetting(water: String){
        binding.btnCultureSetting.text = "관수 푸시알림 주기: ${water}일"
        waterAlarmInterval = water.toInt()
    }

    fun setTemperatureSetting(start: String, end: String){
        binding.btnTemperatureSetting.text = "온도: ${start}~${end}℃"
        if(start.isNotEmpty())
            temStart = start.toInt()
        if(end.isNotEmpty())
            temEnd = end.toInt()
    }

    private fun setScreen(detailToMineDto: DetailToMineDto){
        val waterString = detailToMineDto.water
        var waterFloat = when(waterString){
            CultureSettings.WATER_ARRAY[1] -> 1.0f
            CultureSettings.WATER_ARRAY[2] -> 2.0f
            CultureSettings.WATER_ARRAY[3] -> 3.0f
            CultureSettings.WATER_ARRAY[4] -> 4.0f
            else -> 0.0f
        }

        val sunshine: String = detailToMineDto.sunshine.split("(")[0]

        binding.ratingBarWater.rating = waterFloat
        binding.waterAmount.text = waterString

        var sunshineFloat = when(sunshine){
            "높은 광도" -> 3.0f
            "중간 광도" -> 2.0f
            "낮은 광도" -> 1.0f
            else -> 0.0f
        }
        binding.sunshine.text = sunshineFloat.toString()
        binding.ratingBarSunshine.rating = sunshineFloat

        binding.btnTemperatureSetting.text = detailToMineDto.temperature
    }
}