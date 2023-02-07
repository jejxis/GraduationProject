package oasis.team.econg.graduationproject.diaryFragments

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
import oasis.team.econg.graduationproject.DiaryListActivity
import oasis.team.econg.graduationproject.MainActivity
import oasis.team.econg.graduationproject.R
import oasis.team.econg.graduationproject.data.DiaryPlant
import oasis.team.econg.graduationproject.data.PlantsResponseDto
import oasis.team.econg.graduationproject.databinding.FragmentMyPlantBinding
import oasis.team.econg.graduationproject.retrofit.RetrofitManager
import oasis.team.econg.graduationproject.rvAdapter.MyPlantAdapter
import oasis.team.econg.graduationproject.utils.API
import oasis.team.econg.graduationproject.utils.Constants.TAG
import oasis.team.econg.graduationproject.utils.RESPONSE_STATE


class MyPlantFragment : Fragment() {

    lateinit var binding: FragmentMyPlantBinding
    lateinit var main: MainActivity

    var myPlantList: MutableList<PlantsResponseDto> = mutableListOf()
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
        RetrofitManager.instance.getPlants(auth = API.HEADER_TOKEN, completion = {
                responseState, responseBody ->
            when(responseState){
                RESPONSE_STATE.OKAY -> {
                    Log.d(TAG, "MyPlantFragment - loadData(): api call success : ${responseBody.toString()}")
                    myPlantList = responseBody
                    setAdapter()
                }
                RESPONSE_STATE.FAIL -> {
                    Toast.makeText(main, "MyPlantFragment - loadData(): api call error", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "MyPlantFragment - loadData(): api call fail : $responseBody")
                }
            }
        })
    }

    private val onClickedMyPlant = object : MyPlantAdapter.OnItemClickListener{
        override fun onClicked(id: Long) {
            var intent = Intent(main, DiaryListActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }
    }
}