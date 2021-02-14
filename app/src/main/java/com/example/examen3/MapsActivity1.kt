package com.example.examen3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity1 : AppCompatActivity(), OnMapReadyCallback,GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var marker :Marker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps1)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val myHome = LatLng(39.473532690617795, -0.4099243505967721)
        marker= mMap.addMarker(MarkerOptions()
            .position(myHome)
            .title("My House")
            .snippet("Descripcion de mi casa")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.iconsmaps)))

        // Hacemos zoom 15 F
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myHome, 15F))
        // insertamos control zoom
        mMap.uiSettings.isZoomControlsEnabled =true

         googleMap.setOnMarkerClickListener(this)
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        if (p0 != null) {
            if (p0.equals(marker)){
                var lat = marker.position.latitude
                var lon=marker.position.longitude
                Toast.makeText(this, lat.toString()+","+lon.toString(), Toast.LENGTH_SHORT).show()
      //          Toast.makeText(this, "Esta es mi marca", Toast.LENGTH_SHORT).show()
            }
        }

        return false
    }


}