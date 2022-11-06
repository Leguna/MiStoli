package com.arksana.mistoly.ui.maps

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.arksana.mistoly.R
import com.arksana.mistoly.databinding.ActivityMapsBinding
import com.arksana.mistoly.model.UserPreference
import com.arksana.mistoly.services.ApiService
import com.arksana.mistoly.ui.auth.dataStore
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var userPreference: UserPreference
    private val boundsBuilder = LatLngBounds.Builder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setup()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
        getLocation()
    }

    private fun setup() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        userPreference = UserPreference.getInstance(dataStore)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun getLocation() {
        lifecycleScope.launch(Dispatchers.Main) {
            userPreference.getUser().collect {
                UserPreference.getInstance(dataStore).getUser().collect {
                    val response = ApiService.getInstance(this@MapsActivity)
                        .getAllStories(location = 1, token = it.token)

                    for (story in response.listStory) {
                        val storyMarker = LatLng(story.lat, story.lon)
                        mMap.addMarker(
                            MarkerOptions()
                                .position(storyMarker)
                                .title(story.name)
                                .snippet(story.description)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                        )
                        boundsBuilder.include(storyMarker)
                    }

                    val bounds: LatLngBounds = boundsBuilder.build()
                    mMap.animateCamera(
                        CameraUpdateFactory.newLatLngBounds(
                            bounds,
                            resources.displayMetrics.widthPixels,
                            resources.displayMetrics.heightPixels,
                            200
                        )
                    )
                }
            }
        }
    }
}