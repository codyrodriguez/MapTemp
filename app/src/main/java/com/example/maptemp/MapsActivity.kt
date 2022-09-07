package com.example.maptemp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

//MapImports
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

//weather json imports
import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private var temp = "1"

    //val df = DecimalFormat("#.##")



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


            getWeather(location) //get the weather based on the marker location



            mMap.addMarker(

                MarkerOptions().position(location).title("Temp is ${((temp.toDouble() - 273.15)*1.8+32).toInt()} °F, at Lat: ${String.format("%.3f",location.latitude )}, Long: ${String.format("%.3f",location.longitude )}")
            ) //add marker at location

            mMap.moveCamera(CameraUpdateFactory.newLatLng(location)) //Set up camera based on the location

        }



    }


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

            /*Handler(Looper.getMainLooper()).postDelayed({
                Toast.makeText(this@MapsActivity, "Toast", Toast.LENGTH_SHORT).show()
            }, 1000L)*/



        }

    }



}
