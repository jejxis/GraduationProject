package oasis.team.econg.graduationproject.diaryFragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import oasis.team.econg.graduationproject.DiaryListActivity
import oasis.team.econg.graduationproject.MainActivity
import oasis.team.econg.graduationproject.data.PlantsResponseDto
import oasis.team.econg.graduationproject.databinding.FragmentMyPlantBinding
import oasis.team.econg.graduationproject.retrofit.RetrofitManager
import oasis.team.econg.graduationproject.rvAdapter.MyPlantAdapter
import oasis.team.econg.graduationproject.samplePreference.MyApplication
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
        binding.myPlantList.addItemDecoration(DividerItemDecoration(main, LinearLayout.VERTICAL))
        binding.myPlantList.layoutManager = LinearLayoutManager(main,
        LinearLayoutManager.VERTICAL, false)
        myPlantAdapter.listener = onClickedMyPlant
        binding.myPlantList.adapter = myPlantAdapter
    }

    private fun loadData(){
        RetrofitManager.instance.getPlants(auth = MyApplication.prefs.token, completion = {
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
                else -> {
                    Toast.makeText(main, "데이터를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private val onClickedMyPlant = object : MyPlantAdapter.OnItemClickListener{
        override fun onClicked(id: Long, name: String) {
            var intent = Intent(main, DiaryListActivity::class.java)
            intent.putExtra("id", id)
            intent.putExtra("name", name)
            startActivity(intent)
        }
    }
}