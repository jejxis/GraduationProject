package oasis.team.econg.graduationproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import oasis.team.econg.graduationproject.databinding.ActivityAddPlantBinding

class AddPlantActivity : AppCompatActivity() {
    val binding by lazy{ActivityAddPlantBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.ratingBarSunshine.setOnRatingBarChangeListener {
                ratingBar, fl, b
            ->  binding.sunshine.text = fl.toString()
        }

        binding.ratingBarWater.setOnRatingBarChangeListener {
                ratingBar, fl, b
            ->  binding.waterAmount.text = fl.toString()
        }
    }
}