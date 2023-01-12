package oasis.team.econg.graduationproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import oasis.team.econg.graduationproject.data.PlantSpecies
import oasis.team.econg.graduationproject.databinding.ActivityMineBinding
import oasis.team.econg.graduationproject.rvAdapter.PlantSpeciesAdapter

class MineActivity : AppCompatActivity() {
    val binding by lazy { ActivityMineBinding.inflate(layoutInflater) }
    var myList = mutableListOf<PlantSpecies>()
    lateinit var mineAdapter: PlantSpeciesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        loadData()
        setAdapter()
    }

    private fun setAdapter() {
        mineAdapter = PlantSpeciesAdapter(this)
        mineAdapter.setData(myList)
        mineAdapter.listener = listener
        binding.rvMine.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvMine.adapter =mineAdapter

    }

    private fun loadData() {
        for (i in 0..11) {
            myList.add(
                PlantSpecies(
                    "$i",
                    "PLANT_SPECIES$i",
                    ""
                )
            )
        }
    }

    private val listener = object : PlantSpeciesAdapter.OnClickedItem {
        override fun onClick(id: String) {
            val intent = Intent(this@MineActivity, DetailPlantSpeciesActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }
    }
}