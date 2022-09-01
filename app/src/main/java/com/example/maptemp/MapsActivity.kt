package com.example.maptemp

import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

//MapImports
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.maptemp.databinding.ActivityMapsBinding

//weather json imports
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    var temp = ""


    private fun getWeather(latLng: LatLng) {
        val weatherApi = RetrofitHelper
            .getInstance()
            .create(WeatherApi::class.java)
        // launching a new coroutine
        GlobalScope.launch {
            val result =
                weatherApi.getWeather(latLng.latitude.toString(), latLng.longitude.toString())
            if (result != null)
            // Checking the results
                Log.d("mapTemp11111: ", result.body().toString())
            temp = result.body()?.main?.temp.toString()

            Handler(Looper.getMainLooper()).postDelayed({
                Toast.makeText(this@MapsActivity, "Toast", Toast.LENGTH_SHORT).show()
            }, 1000L)



        }

    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_maps)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera]

        mMap.setOnMapLongClickListener { location ->

            getWeather(location)

            mMap.addMarker(
                MarkerOptions().position(location).title("Temp is $temp, at Lat: ${location.latitude.roundToInt()}, Long: ${location.longitude.roundToInt()}")
            ) //add marker at location

            mMap.moveCamera(CameraUpdateFactory.newLatLng(location)) //Set up camera based on the location

        }

        // mMap.setOnMapLongClickListener {  }

        //  mMap.addMarker(MarkerOptions().position(location).title("Marker in $location"))

        //  mMap.moveCamera(CameraUpdateFactory.newLatLng(location))
    }


}
