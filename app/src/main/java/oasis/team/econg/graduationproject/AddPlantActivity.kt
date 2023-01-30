package oasis.team.econg.graduationproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import oasis.team.econg.graduationproject.databinding.ActivityAddPlantBinding
import oasis.team.econg.graduationproject.dialog.CultureSettingFragment
import oasis.team.econg.graduationproject.dialog.DatePickerFragment
import oasis.team.econg.graduationproject.dialog.TemperatureSettingFragment

class AddPlantActivity : AppCompatActivity() {
    val binding by lazy{ActivityAddPlantBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val pictureAdder = PictureAdder(this, binding.plantThumb)

        binding.ratingBarSunshine.setOnRatingBarChangeListener {
                ratingBar, fl, b
            ->  binding.sunshine.text = fl.toString()
        }

        binding.ratingBarWater.setOnRatingBarChangeListener {
                ratingBar, fl, b
            ->  binding.waterAmount.text = fl.toString()
        }

        binding.plantThumb.setOnClickListener {
            pictureAdder.launch()
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

        }
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
    }

    fun setTemperatureSetting(start: String, end: String){
        binding.btnTemperatureSetting.text = "온도: ${start}℃ ~ ${end}℃"
    }
}