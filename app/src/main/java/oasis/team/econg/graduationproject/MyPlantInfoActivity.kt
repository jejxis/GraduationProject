package oasis.team.econg.graduationproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import oasis.team.econg.graduationproject.databinding.ActivityMyPlantInfoBinding

class MyPlantInfoActivity : AppCompatActivity() {
    val binding by lazy { ActivityMyPlantInfoBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}