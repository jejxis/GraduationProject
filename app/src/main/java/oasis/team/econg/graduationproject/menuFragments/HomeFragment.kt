package oasis.team.econg.graduationproject.menuFragments

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import oasis.team.econg.graduationproject.AddPlantActivity
import oasis.team.econg.graduationproject.DiaryListActivity
import oasis.team.econg.graduationproject.GpsTransfer
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
    private var locationPermissionGranted = false
    private var weatherMamp = HashMap<String, String>()
    private var gpsTransfer: GpsTransfer? = null

    var plants: MutableList<PlantsResponseDto> = mutableListOf()
    var location: Location? = null

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

        getLocationUpdated()
        if(location != null && gpsTransfer != null){
            getCurrentWeather()
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

    private fun getCurrentWeather(){
        RetrofitManager.instance.getWeather(auth = API.HEADER_TOKEN, x = gpsTransfer!!.getYLng().toString(), y = gpsTransfer!!.getXLat().toString(), completion = {
            responseState, responseBody ->
            when(responseState){
                RESPONSE_STATE.OKAY -> {
                    Log.d(TAG, "HomeFragment - getCurrentWeather(): api call success : ${responseBody.toString()}")
                    weatherMamp = responseBody
                    setWeather()
                }
                RESPONSE_STATE.FAIL -> {
                    Toast.makeText(main, "HomeFragment - getCurrentWeather(): api call error", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "HomeFragment - getCurrentWeather(): api call fail : $responseBody")
                }
            }
        })
    }

    private fun setWeather() {
        binding.weatherInfo.text = "기온: ${weatherMamp["T1H"]}°C\n하늘상태: ${weatherMamp["SKY"]}"
    }

    private fun getLocationUpdated(){
        val locationManager: LocationManager = main.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                main,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                main,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted = true
        }else{
            ActivityCompat.requestPermissions(main, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 10.0f, locationListener)
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        gpsTransfer = GpsTransfer(location!!.latitude, location!!.longitude)
        if(gpsTransfer!=null){
            gpsTransfer!!.transfer()
        }
    }

    val locationListener = object : android.location.LocationListener {
        override fun onLocationChanged(location: Location) {
            // 위치가 변경되었을 때 호출됩니다.
            val latitude = location.latitude
            val longitude = location.longitude
        }

        override fun onLocationChanged(locations: MutableList<Location>) {
            super.onLocationChanged(locations)
        }
        override fun onProviderDisabled(provider: String) {
            super.onProviderDisabled(provider)
        }

        override fun onProviderEnabled(provider: String) {
            super.onProviderEnabled(provider)
        }

        override fun onFlushComplete(requestCode: Int) {
            super.onFlushComplete(requestCode)
        }
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