package oasis.team.econg.graduationproject.searchFragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import oasis.team.econg.graduationproject.DetailPlantSpeciesActivity
import oasis.team.econg.graduationproject.MainActivity
import oasis.team.econg.graduationproject.R
import oasis.team.econg.graduationproject.data.GardenDto
import oasis.team.econg.graduationproject.databinding.FragmentAllGardenBinding
import oasis.team.econg.graduationproject.retrofit.RetrofitManager
import oasis.team.econg.graduationproject.rvAdapter.PlantSpeciesAdapter
import oasis.team.econg.graduationproject.utils.API
import oasis.team.econg.graduationproject.utils.RESPONSE_STATE

class AllGardenFragment : Fragment() {

    lateinit var binding: FragmentAllGardenBinding
    lateinit var main: MainActivity

    var gardenList = mutableListOf<GardenDto>()
    lateinit var gardenAdapter: PlantSpeciesAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        main = context as MainActivity
        gardenAdapter = PlantSpeciesAdapter(main)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAllGardenBinding.inflate(inflater, container, false)
        loadData()
        return binding.root
    }

    private fun loadData(){
        RetrofitManager.instance.getGarden(auth = API.HEADER_TOKEN, completion = {
            responseState, responseBody ->
            when(responseState){
                RESPONSE_STATE.OKAY -> {
                    gardenList = responseBody
                    setAdapter()
                }
                RESPONSE_STATE.FAIL -> {
                    Toast.makeText(main, "데이터를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun setAdapter() {
        gardenAdapter = PlantSpeciesAdapter(main)
        gardenAdapter.setData(gardenList)
        gardenAdapter.listener = listener
        binding.rvAllGarden.layoutManager = LinearLayoutManager(main, LinearLayoutManager.VERTICAL, false)
        binding.rvAllGarden.adapter = gardenAdapter
    }

    private val listener = object: PlantSpeciesAdapter.OnClickedItem{
        override fun onClick(id: Long) {
            val intent = Intent(main, DetailPlantSpeciesActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }
    }
}