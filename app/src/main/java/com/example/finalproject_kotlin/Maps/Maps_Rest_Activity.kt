package com.example.finalproject_kotlin.Maps

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.finalproject_kotlin.R
import com.example.finalproject_kotlin.databinding.ActivityMapsRestBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Maps_Rest_Activity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsRestBinding
    private var firebaseAuth: FirebaseAuth? = null
    private var firestore: FirebaseFirestore? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        binding = ActivityMapsRestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
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
        mMap.uiSettings.isCompassEnabled = true // خيار البوصلة
        mMap.uiSettings.isMyLocationButtonEnabled = true // بجيبلك موقعك الحالي
        mMap.uiSettings.isRotateGesturesEnabled = true // عملية تدوير للخريطة

        getData(mMap)
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
    }

    // دالة بتجيب الموقع على حسب الاسم
    fun getLatLng(context: Context, addressS: String): LatLng? {
        var geocoder = Geocoder(context)
        var address: List<Address>? = null
        var latlng: LatLng? = null
        try {
            address = geocoder.getFromLocationName(
                addressS, 4
            ) // على حسب الاسم بتجيب الاحداثيااات تبعت الموقع
            if (address == null) {
                return null
            }
            var lnlg = address.get(0)
            latlng = LatLng(lnlg.latitude, lnlg.longitude)
        } catch (e: java.lang.Exception) {
            Log.e("yazan", "()()()()()()" + e.message.toString())
        }
        return latlng
    }

    fun getData(googleMap: GoogleMap) {
        firestore!!.collection("restaurant").get().addOnSuccessListener { data ->
            for (dd in data) {
                /*pro=product(dd.get("name").toString(),dd.get("price").toString())
                arrLis.add(pro)*/
                var lat: LatLng? = null
                try {
                    lat = getLatLng(this, dd.get("name").toString())
                    mMap.addMarker(
                        MarkerOptions().position(lat!!).title(dd.get("name").toString())
                            .snippet(dd.get("desc").toString())
                    )
                    //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lat,28f))
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(lat))
                    mMap.uiSettings.isZoomControlsEnabled = true
                    Log.e("yazan", "++++++++=========" + lat.toString())
                } catch (e: java.lang.Exception) {
                    Log.e("yazan", "///*/*/*/*/*" + e.message.toString())
                }
            }

        }.addOnFailureListener {
            Log.e("yazan", it.message.toString())
        }
    }
}