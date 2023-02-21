package oasis.team.econg.graduationproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import oasis.team.econg.graduationproject.data.GardenDto
import oasis.team.econg.graduationproject.data.PlantSpecies
import oasis.team.econg.graduationproject.databinding.ActivityFavoritePlantBinding
import oasis.team.econg.graduationproject.rvAdapter.PlantSpeciesAdapter

class FavoritePlantActivity : AppCompatActivity() {
    val binding by lazy{ActivityFavoritePlantBinding.inflate(layoutInflater)}
    var plantSpeciesList = mutableListOf<GardenDto>()
    lateinit var favoriteAdapter: PlantSpeciesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        loadData()
        setAdapter()
    }

    private fun setAdapter() {
        favoriteAdapter = PlantSpeciesAdapter(this)
        favoriteAdapter.setData(plantSpeciesList)
        favoriteAdapter.listener = listener
        binding.rvFavorite.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvFavorite.adapter = favoriteAdapter

    }

    private fun loadData(){

    }

    private val listener = object: PlantSpeciesAdapter.OnClickedItem{
        override fun onClick(id: Long) {
            val intent = Intent(this@FavoritePlantActivity, DetailPlantSpeciesActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }
    }
}