package oasis.team.econg.graduationproject.diaryFragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import oasis.team.econg.graduationproject.DiaryListActivity
import oasis.team.econg.graduationproject.MainActivity
import oasis.team.econg.graduationproject.R
import oasis.team.econg.graduationproject.data.DiaryPlant
import oasis.team.econg.graduationproject.databinding.FragmentMyPlantBinding
import oasis.team.econg.graduationproject.rvAdapter.MyPlantAdapter


class MyPlantFragment : Fragment() {

    lateinit var binding: FragmentMyPlantBinding
    lateinit var main: MainActivity

    var myPlantList: MutableList<DiaryPlant> = mutableListOf()
    lateinit var myPlantAdapter: MyPlantAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        main = context as MainActivity
        myPlantAdapter = MyPlantAdapter(main)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyPlantBinding.inflate(inflater,container, false)

        loadData()
        setAdapter()

        return binding.root
    }

    private fun setAdapter() {
        myPlantAdapter.setData(myPlantList)
        binding.myPlantList.layoutManager = LinearLayoutManager(main,
        LinearLayoutManager.VERTICAL, false)
        myPlantAdapter.listener = onClickedMyPlant
        binding.myPlantList.adapter = myPlantAdapter
    }

    private fun loadData(){
        for(i in 0..7){
            myPlantList.add(
                DiaryPlant(i.toString(),"plant$i", R.drawable.ic_baseline_eco_45,"2023.01.0$i",
                    i%2==0)
            )
        }
    }

    private val onClickedMyPlant = object : MyPlantAdapter.OnItemClickListener{
        override fun onClicked(id: String) {
            var intent = Intent(main, DiaryListActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }
    }
}