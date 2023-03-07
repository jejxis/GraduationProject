package oasis.team.econg.graduationproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import oasis.team.econg.graduationproject.data.GardenDto
import oasis.team.econg.graduationproject.databinding.ActivityFavoritePlantBinding
import oasis.team.econg.graduationproject.retrofit.RetrofitManager
import oasis.team.econg.graduationproject.rvAdapter.PlantSpeciesAdapter
import oasis.team.econg.graduationproject.samplePreference.MyApplication
import oasis.team.econg.graduationproject.utils.API
import oasis.team.econg.graduationproject.utils.RESPONSE_STATE

class FavoritePlantActivity : AppCompatActivity() {
    val binding by lazy{ActivityFavoritePlantBinding.inflate(layoutInflater)}
    var plantSpeciesList = mutableListOf<GardenDto>()
    lateinit var favoriteAdapter: PlantSpeciesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }

        loadData()
    }

    private fun setAdapter() {
        favoriteAdapter = PlantSpeciesAdapter(this)
        favoriteAdapter.setData(plantSpeciesList)
        favoriteAdapter.listener = listener
        binding.rvFavorite.addItemDecoration(DividerItemDecoration(this, LinearLayout.VERTICAL))
        binding.rvFavorite.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvFavorite.adapter = favoriteAdapter

    }

    private fun loadData(){
        RetrofitManager.instance.getBookmarks(auth = MyApplication.prefs.token, completion = {
            responseState, responseBody ->
            when(responseState){
                RESPONSE_STATE.OKAY -> {
                    plantSpeciesList = responseBody
                    setAdapter()
                }
                RESPONSE_STATE.FAIL -> {
                    Toast.makeText(this@FavoritePlantActivity, "데이터를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private val listener = object: PlantSpeciesAdapter.OnClickedItem{
        override fun onClick(id: Long) {
            val intent = Intent(this@FavoritePlantActivity, DetailPlantSpeciesActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }
    }
}