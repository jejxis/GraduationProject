package oasis.team.econg.graduationproject.menuFragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import oasis.team.econg.graduationproject.AddPlantActivity
import oasis.team.econg.graduationproject.DiaryListActivity
import oasis.team.econg.graduationproject.MainActivity
import oasis.team.econg.graduationproject.data.PlantsResponseDto
import oasis.team.econg.graduationproject.databinding.FragmentHomeBinding
import oasis.team.econg.graduationproject.location.LocationProvider
import oasis.team.econg.graduationproject.location.MyLocation
import oasis.team.econg.graduationproject.retrofit.RetrofitManager
import oasis.team.econg.graduationproject.rvAdapter.HomeDiaryAdapter
import oasis.team.econg.graduationproject.samplePreference.MyApplication
import oasis.team.econg.graduationproject.utils.*
import oasis.team.econg.graduationproject.utils.Constants.GUIDELINE
import oasis.team.econg.graduationproject.utils.Constants.TAG


class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    lateinit var main: MainActivity
    lateinit var locationProvider: LocationProvider
    private var weatherMamp = HashMap<String, String>()

    var plants: MutableList<PlantsResponseDto> = mutableListOf()

    lateinit var homeDiaryAdapter: HomeDiaryAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        main = context as MainActivity
        locationProvider = LocationProvider(main, weatherSetter)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater,container, false)
        binding.hello.text = "안녕하세요 ${MyApplication.prefs.nickname}님!"
        binding.btnGoToGuide.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(GUIDELINE))
            startActivity(intent)
        }
        binding.btnAddPlant.setOnClickListener {
            Log.d("TAG", "onAttach: click")
            var intent = Intent(main, AddPlantActivity::class.java)
            startActivity(intent)
        }
        locationProvider.getLocationUpdated()
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
        RetrofitManager.instance.getPlants(auth = MyApplication.prefs.token, completion = {
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
                else -> {
                    Toast.makeText(main, "데이터를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun setAdapter(){
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.diaryList)
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

    private val weatherSetter = object : LocationProvider.WeatherSetter{
        override fun getCurrentWeather(myLocation: MyLocation){
            RetrofitManager.instance.getWeather(auth = MyApplication.prefs.token, x = myLocation.x.toInt().toString(), y = myLocation.y.toInt().toString(), completion = {
                    responseState, responseBody ->
                when(responseState){
                    RESPONSE_STATE.OKAY -> {
                        Log.d(TAG, "HomeFragment - getCurrentWeather(): api call success : ${responseBody.toString()}")
                        weatherMamp = responseBody
                        setWeather()
                    }
                    RESPONSE_STATE.FAIL -> {
                        Toast.makeText(main, "날씨 정보를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "HomeFragment - getCurrentWeather(): api call fail : $responseBody")
                    }
                    else -> {
                        Toast.makeText(main, "날씨 정보를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }

        override fun setWeather() {
            var weather = ""
            if(weatherMamp["PTY"] == "0"){
                weather = skyList[weatherMamp["SKY"]!!.toInt()]
            }
            else{
                weather = ptyList[weatherMamp["PTY"]!!.toInt()]
            }
            binding.weatherInfo.text = weather
            binding.weatherTemp.text = "${weatherMamp["T1H"]}°C"
            var weatherPicture = weatherIconSetMap[weather]
            if(weatherPicture != null)
                binding.weatherIcon.setImageResource(weatherPicture!!)
            binding.weatherWind.text = "${weatherMamp["WSD"]}m/s"
            binding.weatherHumidity.text = "${weatherMamp["REH"]}%"
            if(weatherMamp["RN1"] == "강수없음") binding.weatherRain.text = weatherMamp["RN1"]
            else
                binding.weatherRain.text = "${weatherMamp["RN1"]}mm"
        }
    }
}