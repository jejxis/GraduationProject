package oasis.team.econg.graduationproject.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import oasis.team.econg.graduationproject.GpsTransfer
import oasis.team.econg.graduationproject.MainActivity
import oasis.team.econg.graduationproject.utils.Constants

class LocationProvider(context: Context, setter: LocationProvider.WeatherSetter) {
    var main: MainActivity = context as MainActivity
    private var gpsTransfer: GpsTransfer? = null
    private var location: Location? = null
    private var targetLocation: MyLocation? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var setter = setter
    private val locationListener = object : android.location.LocationListener {
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

    fun getLocation(): MyLocation{
        return targetLocation!!
    }

    fun getLocationUpdated(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(main)
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
                        Log.d(Constants.TAG, "getLocationUpdated: l is null")
                    else {
                        location = l
                        Log.d(Constants.TAG, "getLocationUpdated: location: $location")
                        gpsTransfer = GpsTransfer(location!!.latitude, location!!.longitude)
                        if(gpsTransfer!=null){
                            Log.d(Constants.TAG, "getLocationUpdated: gpsTransfer is not null")
                            gpsTransfer!!.transfer()
                            targetLocation = MyLocation(x = gpsTransfer!!.getXLat(),y = gpsTransfer!!.getYLng())
                            setter.getCurrentWeather(targetLocation!!)
                        }
                    }

                }
        }
        else{
            gpsTransfer = GpsTransfer(location!!.latitude, location!!.longitude)
            if(gpsTransfer!=null){
                gpsTransfer!!.transfer()
                targetLocation = MyLocation(x = gpsTransfer!!.getXLat(),y = gpsTransfer!!.getYLng())
            }
        }
    }
    interface WeatherSetter{
        fun getCurrentWeather(myLocation: MyLocation)
        fun setWeather()
    }
}
data class MyLocation(var x: Double, var y: Double)