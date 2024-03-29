package oasis.team.econg.graduationproject.searchFragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import oasis.team.econg.graduationproject.DetailPlantSpeciesActivity
import oasis.team.econg.graduationproject.MainActivity
import oasis.team.econg.graduationproject.data.GardenDto
import oasis.team.econg.graduationproject.databinding.FragmentSearchResultBinding
import oasis.team.econg.graduationproject.retrofit.RetrofitManager
import oasis.team.econg.graduationproject.rvAdapter.PlantSpeciesAdapter
import oasis.team.econg.graduationproject.samplePreference.MyApplication
import oasis.team.econg.graduationproject.utils.API
import oasis.team.econg.graduationproject.utils.RESPONSE_STATE

class SearchResultFragment : Fragment() {

    lateinit var binding: FragmentSearchResultBinding
    lateinit var main: MainActivity

    var plantSpeciesList = mutableListOf<GardenDto>()
    lateinit var plantSearchAdapter: PlantSpeciesAdapter

    private val KEY = "KEY"
    fun newInstance(data: String) = SearchResultFragment().apply{
        arguments = Bundle().apply{
            putString(KEY, data)
        }
    }

    private val key by lazy{requireArguments().getString(KEY)}

    override fun onAttach(context: Context) {
        super.onAttach(context)
        main = context as MainActivity
        plantSearchAdapter = PlantSpeciesAdapter(main)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchResultBinding.inflate(inflater, container, false)

        if(!key.isNullOrEmpty()){
            loadData()
        }

        return binding.root
    }

    private fun setAdapter() {
        plantSearchAdapter = PlantSpeciesAdapter(main)
        plantSearchAdapter.setData(plantSpeciesList)
        plantSearchAdapter.listener = listener
        binding.rvSearch.addItemDecoration(DividerItemDecoration(main, LinearLayout.VERTICAL))
        binding.rvSearch.layoutManager = LinearLayoutManager(main, LinearLayoutManager.VERTICAL, false)
        binding.rvSearch.adapter = plantSearchAdapter

    }

    private fun loadData(){
        RetrofitManager.instance.searchGarden(auth = MyApplication.prefs.token, keyword = key!!, completion = {
            responseState, responseBody ->
            when(responseState){
                RESPONSE_STATE.OKAY->{
                    plantSpeciesList = responseBody
                    if(plantSpeciesList.size < 1){
                        Toast.makeText(main, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show()
                    }
                    setAdapter()
                }
                RESPONSE_STATE.FAIL->{
                    Toast.makeText(main, "데이터를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(main, "데이터를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private val listener = object: PlantSpeciesAdapter.OnClickedItem{
        override fun onClick(id: Long) {
            val intent = Intent(main, DetailPlantSpeciesActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }
    }
}