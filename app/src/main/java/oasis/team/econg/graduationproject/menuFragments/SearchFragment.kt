package oasis.team.econg.graduationproject.menuFragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import oasis.team.econg.graduationproject.DetailPlantSpeciesActivity
import oasis.team.econg.graduationproject.MainActivity
import oasis.team.econg.graduationproject.data.PlantSpecies
import oasis.team.econg.graduationproject.databinding.FragmentSearchBinding
import oasis.team.econg.graduationproject.rvAdapter.PlantSpeciesAdapter
import oasis.team.econg.graduationproject.utils.Constants.GUIDELINE

class SearchFragment : Fragment() {

    lateinit var binding: FragmentSearchBinding
    lateinit var main: MainActivity

    var plantSpeciesList = mutableListOf<PlantSpecies>()
    lateinit var plantSearchAdapter: PlantSpeciesAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        main = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        binding.searchView.setOnClickListener {
            binding.rvSearch.visibility = View.VISIBLE
            binding.guidelineLayout.visibility = View.GONE
            loadData()
            setAdapter()
        }

        binding.btnGuideline.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(GUIDELINE))
            startActivity(intent)
        }

        return binding.root
    }

    private fun setAdapter() {
        plantSearchAdapter = PlantSpeciesAdapter(main)
        plantSearchAdapter.setData(plantSpeciesList)
        plantSearchAdapter.listener = listener
        binding.rvSearch.layoutManager = LinearLayoutManager(main, LinearLayoutManager.VERTICAL, false)
        binding.rvSearch.adapter = plantSearchAdapter

    }

    private fun loadData(){
        for(i in 0..11){
            plantSpeciesList.add(
                PlantSpecies(
                    "$i",
                    "PLANT_SPECIES$i",
                    ""
                )
            )
        }
    }

    private val listener = object: PlantSpeciesAdapter.OnClickedItem{
        override fun onClick(id: String) {
            val intent = Intent(main, DetailPlantSpeciesActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }
    }

}