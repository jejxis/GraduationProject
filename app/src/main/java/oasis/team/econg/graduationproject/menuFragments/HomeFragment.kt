package oasis.team.econg.graduationproject.menuFragments

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import oasis.team.econg.graduationproject.AddPlantActivity
import oasis.team.econg.graduationproject.DiaryListActivity
import oasis.team.econg.graduationproject.MainActivity
import oasis.team.econg.graduationproject.R
import oasis.team.econg.graduationproject.data.Plant
import oasis.team.econg.graduationproject.databinding.FragmentHomeBinding
import oasis.team.econg.graduationproject.rvAdapter.DiaryAdapter
import oasis.team.econg.graduationproject.rvAdapter.HomeDiaryAdapter


class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    lateinit var main: MainActivity

    var plants: MutableList<Plant> = mutableListOf()

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
        homeDiaryAdapter = HomeDiaryAdapter(main)
        loadData()

        return binding.root
    }

    private fun loadData(){
        for(i in 0..8){
            var plant = Plant(plantId = "$i", hum = i+10.0, temp = i*5.0, name="${i}번째 식물", days = i*100)
            plants.add(plant)
        }
        homeDiaryAdapter.setData(plants)
        binding.diaryList.layoutManager = LinearLayoutManager(main,
        LinearLayoutManager.HORIZONTAL, false)
        homeDiaryAdapter.listener = onClickedListItem
        binding.diaryList.adapter = homeDiaryAdapter
    }

    private val onClickedListItem = object : HomeDiaryAdapter.OnItemClickListener{
        override fun onClicked(id: String) {
            val intent = Intent(main, DiaryListActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }
    }
}