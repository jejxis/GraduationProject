package oasis.team.econg.graduationproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import oasis.team.econg.graduationproject.databinding.ActivityAddDiaryBinding

class AddDiaryActivity : AppCompatActivity() {
    val binding by lazy{ActivityAddDiaryBinding.inflate(layoutInflater)}
    var id: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        var pictureAdder = PictureAdder(this, binding.plantImage)

        if(intent.hasExtra("id")){
            id = intent.getStringExtra("id").toString()
        }

        binding.btnClose.setOnClickListener {
            finish()
        }

        binding.addPicture.setOnClickListener {
            pictureAdder.launch()
        }

        binding.btnAddDiary.setOnClickListener {
            finish()
        }
    }
}