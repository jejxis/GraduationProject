package oasis.team.econg.graduationproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import oasis.team.econg.graduationproject.databinding.ActivityDetailPlantSpeciesBinding

class DetailPlantSpeciesActivity : AppCompatActivity() {
    val binding by lazy{ActivityDetailPlantSpeciesBinding.inflate(layoutInflater)}
    var id: String = ""
    var bookmark = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if(intent.hasExtra("id")){
            id = intent.getStringExtra("id").toString()
        }

        setScreen()

        binding.btnBookmark.setOnClickListener {
            if(!bookmark){
                binding.btnBookmark.setImageResource(R.drawable.ic_baseline_bookmark_45)
            }
            else
                binding.btnBookmark.setImageResource(R.drawable.ic_baseline_bookmark_border_45)
            bookmark = !bookmark
        }

        binding.btnClose.setOnClickListener {
            finish()
        }
    }

    private fun setScreen() {
        binding.plantSpeciesName.text = "식물 종 No.$id"
        binding.plantDescription.text = "것이 따뜻한 봄바람이다 인생에 따뜻한 봄바람을 불어 보내는 것은 청춘의 끓는 피다 청춘의 피가 뜨거운지라 인간의 동산에는 사랑의풀이 돋고 이상의 꽃이 피고 희망의 놀이 뜨고열락의 새가 운다사랑의 풀이 없으면 인간은"
    }
}