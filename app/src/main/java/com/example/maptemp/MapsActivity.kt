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

    var temp = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

            runOnUiThread {
                setMarker(latLng)
            }
        }
    }

    private fun setMarker(latLng: LatLng) {
        mMap.addMarker(

            MarkerOptions().position(latLng).title(
                "Temp is ${((temp.toDouble() - 273.15) * 1.8 + 32).toInt()} °F, at Lat: ${
                    String.format(
                        "%.3f",
                        latLng.latitude
                    )
                }, Long: ${String.format("%.3f", latLng.longitude)}"
            )
        ) //add marker at location
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng)) //Set up camera based on the location
    }
}
