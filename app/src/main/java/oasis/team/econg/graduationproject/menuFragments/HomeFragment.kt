package oasis.team.econg.graduationproject.menuFragments

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.net.Uri
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
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_SETTLING
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import oasis.team.econg.graduationproject.AddPlantActivity
import oasis.team.econg.graduationproject.DiaryListActivity
import oasis.team.econg.graduationproject.GpsTransfer
import oasis.team.econg.graduationproject.MainActivity
import oasis.team.econg.graduationproject.data.PlantsResponseDto
import oasis.team.econg.graduationproject.databinding.FragmentHomeBinding
import oasis.team.econg.graduationproject.retrofit.RetrofitManager
import oasis.team.econg.graduationproject.rvAdapter.HomeDiaryAdapter
import oasis.team.econg.graduationproject.samplePreference.MyApplication
import oasis.team.econg.graduationproject.utils.*
import oasis.team.econg.graduationproject.utils.Constants.GUIDELINE
import oasis.team.econg.graduationproject.utils.Constants.TAG


class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    lateinit var main: MainActivity
    private var locationPermissionGranted = false
    private var weatherMamp = HashMap<String, String>()
    private var gpsTransfer: GpsTransfer? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient

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
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(main)
        getLocationUpdated()
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

    private fun getCurrentWeather(){
        RetrofitManager.instance.getWeather(auth = MyApplication.prefs.token, x = gpsTransfer!!.getXLat().toInt().toString(), y = gpsTransfer!!.getYLng().toInt().toString(), completion = {
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

    private fun setWeather() {
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

    private fun getLocationUpdated(){
        val locationManager: LocationManager = main.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                main,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                main,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(main, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                1
            )
        }else{
            locationPermissionGranted = true
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 10.0f, locationListener)

        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if(location == null){
            fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, object : CancellationToken() {
                override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token

                override fun isCancellationRequested() = false
            })
                .addOnSuccessListener { l: Location? ->
                    if (l== null)
                        Log.d(TAG, "getLocationUpdated: l is null")
                    else {
                        location = l
                        Log.d(TAG, "getLocationUpdated: location: $location")
                        gpsTransfer = GpsTransfer(location!!.latitude, location!!.longitude)
                        if(gpsTransfer!=null){
                            Log.d(TAG, "getLocationUpdated: gpsTransfer is not null")
                            gpsTransfer!!.transfer()
                            getCurrentWeather()
                        }
                    }

                }
        }
        else{
            gpsTransfer = GpsTransfer(location!!.latitude, location!!.longitude)
            if(gpsTransfer!=null){
                gpsTransfer!!.transfer()
                getCurrentWeather()
            }
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
}