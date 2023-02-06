package oasis.team.econg.graduationproject.menuFragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import oasis.team.econg.graduationproject.AddPlantActivity
import oasis.team.econg.graduationproject.DiaryListActivity
import oasis.team.econg.graduationproject.MainActivity
import oasis.team.econg.graduationproject.data.PlantsResponseDto
import oasis.team.econg.graduationproject.databinding.FragmentHomeBinding
import oasis.team.econg.graduationproject.retrofit.RetrofitManager
import oasis.team.econg.graduationproject.rvAdapter.HomeDiaryAdapter
import oasis.team.econg.graduationproject.utils.API
import oasis.team.econg.graduationproject.utils.Constants.TAG
import oasis.team.econg.graduationproject.utils.RESPONSE_STATE


class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    lateinit var main: MainActivity

    var plants: MutableList<PlantsResponseDto> = mutableListOf()

    lateinit var homeDiaryAdapter: HomeDiaryAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        main = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater,container, false)
        binding.btnAddPlant.setOnClickListener {
            Log.d("TAG", "onAttach: click")
            var intent = Intent(main, AddPlantActivity::class.java)
            startActivity(intent)
        }

        loadData()

        return binding.root
    }

    private fun setNoPlant(){
        binding.btnAddPlant.visibility = View.GONE
        binding.diaryList.visibility = View.GONE
        binding.btnAddPlant.visibility = View.GONE
        binding.btnAddWhenNoPlant.visibility = View.VISIBLE
        binding.noPlantLayout.visibility = View.VISIBLE

        binding.btnAddWhenNoPlant.setOnClickListener {
            var intent = Intent(main, AddPlantActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadData(){
        RetrofitManager.instance.getPlants(auth = API.HEADER_TOKEN, completion = {
            responseState, responseBody ->
            when(responseState){
                RESPONSE_STATE.OKAY -> {
                    Log.d(TAG, "HomeFragment - loadData(): api call success : ${responseBody.toString()}")
                    plants = responseBody
                    if(plants.size < 1){
                        setNoPlant()
                    }
                    else{
                        setAdapter()
                    }
                }
                RESPONSE_STATE.FAIL -> {
                    Toast.makeText(main, "HomeFragment - loadData(): api call error", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "HomeFragment - loadData(): api call fail : $responseBody")
                }
            }
        })
    }

    private fun setAdapter(){
        homeDiaryAdapter = HomeDiaryAdapter(main)
        homeDiaryAdapter.setData(plants)
        binding.diaryList.layoutManager = LinearLayoutManager(main,
            LinearLayoutManager.HORIZONTAL, false)
        homeDiaryAdapter.listener = onClickedListItem
        binding.diaryList.adapter = homeDiaryAdapter
    }

    private val onClickedListItem = object : HomeDiaryAdapter.OnItemClickListener{
        override fun onClicked(id: Long) {
            val intent = Intent(main, DiaryListActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }
    }
}