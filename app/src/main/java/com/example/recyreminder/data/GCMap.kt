package com.example.recyreminder.data

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.recyreminder.R
import com.example.recyreminder.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.io.IOException


class GCMap : AppCompatActivity(), OnMapReadyCallback{

    private var database: DatabaseReference = Firebase.database.reference
    private lateinit var mPrefs: SharedPreferences
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var mContext = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
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

        val starterCoord = LatLng(39.0, -77.0)
        val starterZoom = 6f
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(starterCoord, starterZoom))

        val residents: DatabaseReference =
            database.child("users/residents")

        residents.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (userAddress in snapshot.children) {
                    var addressString = userAddress.key
                    var addressCoords = getLocationFromAddress(mContext, addressString.toString())
                    if(addressCoords != null) {
                        val mMarker = mMap.addMarker(MarkerOptions().position(addressCoords!!).title(addressString.toString()))
//                        mMap.setOnMarkerClickListener {
//                            onMarkerClick(mMarker!!)
//                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(PositionLogin.TAG, error.message)
            }
        })

        mMap.setOnMarkerClickListener {
            onMarkerClick(it)
        }

    }

    fun onMarkerClick(marker: Marker): Boolean {
        //Log.i(TAG, "THIS IS A MARKER CLICKER: " + marker.title)
        var savedAddress = Intent().putExtra("address", marker.title.toString())
        setResult(RESULT_OK, savedAddress)
        finish()
        return true
    }

    fun getLocationFromAddress(context: Context?, address: String?): LatLng? {
        val gCoder = Geocoder(context)

        try {
            val address: List<Address>? = gCoder.getFromLocationName(address, 1)
            if (address == null) {
                return null
            }
            val loc = address[0]

            return LatLng(loc.getLatitude(), loc.getLongitude())
        } catch(exception: IOException) {
            val dur = Toast.LENGTH_LONG
            val toast = Toast.makeText(applicationContext, "Cant load markers right now", dur)
            toast.show()
            exception.printStackTrace()
        }
        return null
    }

    companion object {
        const val TAG = "Recycling Reminder"
    }

}

