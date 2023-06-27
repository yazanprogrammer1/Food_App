package com.example.finalproject_kotlin.Maps

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.finalproject_kotlin.R
import com.example.finalproject_kotlin.databinding.ActivityMapsUserBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class Maps_User_Activity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsUserBinding
    private var firebaseAuth: FirebaseAuth? = null
    private var firestore: FirebaseFirestore? = null
    var latlngs: LatLng? = null
    var latlngsss: LatLng? = null
    lateinit var name: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ///.. code
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        name = intent.getStringExtra("name").toString()
        latlngs = getLatLng(this@Maps_User_Activity, name.toString())
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // Add a marker in Sydney and move the camera
        //......
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        mMap.isMyLocationEnabled = true
        var latude = intent.getDoubleExtra("lat", 0.0)
        var longtude = intent.getDoubleExtra("long", 0.0)
        latlngsss = LatLng(latude, longtude)
        mMap.addMarker(MarkerOptions().position(latlngs!!).title(name))
        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lat,28f))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlngs!!))
        mMap.addMarker(MarkerOptions().position(latlngsss!!).title("Your Location"))
        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lat,28f))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlngsss!!))
        mMap.uiSettings.isCompassEnabled = true // خيار البوصلة
        mMap.uiSettings.isMyLocationButtonEnabled = true // بجيبلك موقعك الحالي
        mMap.uiSettings.isRotateGesturesEnabled = true // عملية تدوير للخريطة
        mMap.addPolyline(
            PolylineOptions()
                .add(latlngs)
                .add(latlngsss)
                .color(Color.GREEN)
                .visible(true)
        )
    }

    fun getLatLng(context: Context, addressS: String): LatLng {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addList = geocoder.getFromLocationName(addressS, 1)
        var lats = addList!![0].latitude
        var lngs = addList[0].longitude
        return LatLng(lats, lngs)

        /*  val geocoder= Geocoder(context)
          var address: List<Address>?=null
          var latlng:LatLng?=null
          try {
              address= geocoder.getFromLocationName(addressS,1)
              if (address==null){
                  return null
              }
              val lnlg=address.get(0)
              latlng= LatLng(lnlg.latitude,lnlg.longitude)


          }catch (e:java.lang.Exception){
              Log.e("abd","()()()()()()"+e.message.toString())
          }
          return

          }
         */

    }
}