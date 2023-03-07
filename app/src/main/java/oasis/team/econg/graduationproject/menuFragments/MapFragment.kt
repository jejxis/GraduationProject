package oasis.team.econg.graduationproject.menuFragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import oasis.team.econg.graduationproject.MainActivity
import oasis.team.econg.graduationproject.data.Document
import oasis.team.econg.graduationproject.databinding.FragmentMapBinding
import oasis.team.econg.graduationproject.retrofit.RetrofitManager
import oasis.team.econg.graduationproject.samplePreference.MyApplication
import oasis.team.econg.graduationproject.utils.API
import oasis.team.econg.graduationproject.utils.Constants.TAG
import oasis.team.econg.graduationproject.utils.RESPONSE_STATE


class MapFragment : Fragment(), OnMapReadyCallback {
    private var map: GoogleMap? = null
    private var cameraPosition: CameraPosition? = null
    private var placeList = mutableListOf<Document>()

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    lateinit var binding: FragmentMapBinding
    lateinit var main: MainActivity

    private val defaultLocation = LatLng(-33.8523341, 151.2106085)
    private var locationPermissionGranted = false

    private var lastKnownLocation: Location? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        main = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMapBinding.inflate(inflater, container, false)

        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION)
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION)
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(main)
        binding.map.getMapAsync(this)

        return binding.root
    }



    override fun onSaveInstanceState(outState: Bundle) {
        map?.let { map ->
            outState.putParcelable(KEY_CAMERA_POSITION, map.cameraPosition)
            outState.putParcelable(KEY_LOCATION, lastKnownLocation)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        setClickListener()
        getLocationPermission()
        updateLocationUI()
        getDeviceLocation()
    }
    private fun setClickListener(){
        map!!.setOnMarkerClickListener(object: GoogleMap.OnMarkerClickListener{
            override fun onMarkerClick(marker: Marker): Boolean {
                binding.info.visibility = View.VISIBLE
                var arr = marker.tag.toString().split("/")
                binding.placeName.text = arr[0]
                binding.phone.text = arr[1]
                binding.address.text = arr[2]
                binding.roadAddress.text = arr[3]
                binding.placeUrl.text = arr[4]

                return false
            }
        })

        map!!.setOnMapClickListener(object: GoogleMap.OnMapClickListener{
            override fun onMapClick(p0: LatLng) {
                binding.info.visibility = View.GONE
            }
        })
    }

    @SuppressLint("MissingPermission")
    private fun getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(main) { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        lastKnownLocation = task.result
                        if (lastKnownLocation != null) {
                            val lat = lastKnownLocation!!.latitude
                            val lng = lastKnownLocation!!.longitude
                            map?.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                LatLng(lat, lng), ZOOM))
                            loadPlaces(lng.toString(), lat.toString())
                        }
                    } else {
                        map?.moveCamera(CameraUpdateFactory
                            .newLatLngZoom(defaultLocation, ZOOM))
                        map?.uiSettings?.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    private fun getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(main.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(main, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }
    }
    private fun loadPlaces(x: String, y: String){
        RetrofitManager.instance.getPlaces(auth = MyApplication.prefs.token, x = x, y = y, completion = {
            responseState, responseBody ->
                when (responseState) {
                    RESPONSE_STATE.OKAY -> {
                        Log.d(TAG, "MapFragment - loadPlaces(): api call success")
                        placeList = responseBody
                        addMarkers()
                    }
                    RESPONSE_STATE.FAIL -> {
                        Toast.makeText(main, "식물샵 정보를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "DiaryListActivity - loadData(): api call fail : $responseBody")
                    }
                }
        })
    }

    private fun addMarkers(){
        placeList.forEach {
            var markerOptions = MarkerOptions()
            markerOptions.title(it.place_name)
                .position(LatLng(it.y.toDouble(), it.x.toDouble()))
                .snippet(it.address_name)
            val marker = map?.addMarker(markerOptions)
            marker?.tag =
                it.place_name+"/"+it.phone+"/"+it.address_name+"/"+it.road_address_name+"/"+it.place_url

        }
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationUI() {
        if (map == null) {
            return
        }
        try {
            if (locationPermissionGranted) {
                map?.isMyLocationEnabled = true
                map?.uiSettings?.isMyLocationButtonEnabled = true
            } else {
                map?.isMyLocationEnabled = false
                map?.uiSettings?.isMyLocationButtonEnabled = false
                lastKnownLocation = null
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    companion object{
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
        private const val KEY_CAMERA_POSITION = "camera_position"
        private const val KEY_LOCATION = "location"
        private const val ZOOM = 15.0f

    }

    override fun onStart() {
        super.onStart()
        binding.map.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.map.onStop()
    }

    override fun onResume() {
        super.onResume()
        binding.map.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.map.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.map.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.map.onDestroy()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if(binding.map != null){
            binding.map.onCreate(savedInstanceState)
        }
    }
}