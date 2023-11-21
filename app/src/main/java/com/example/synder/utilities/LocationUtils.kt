package com.example.synder.utilities
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import javax.inject.Inject

class LocationUtils @Inject constructor(private val context: Context){
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    private var locationCallback: LocationCallback? = null

    fun getCurrentLocation(onLocationResult: (Location) -> Unit, onFailure: (Exception) -> Unit){

        if (checkLocationPermission()){
        val locationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(10000)
            .setFastestInterval(5000)

        val locationCallback = object : LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult?.lastLocation?.let { onLocationResult.invoke(it)}
            }
            override fun onLocationAvailability(locationAvailability: LocationAvailability?){
                //handle location availability if needed
            }
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
        } else {
            onFailure.invoke(SecurityException("Location Permission not granted"))
        }
    }

    fun checkLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context, android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestLocationPermission(activity: Activity, requestCode: Int){
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
            requestCode
        )
    }

    fun stopLocationUpdates(){
        locationCallback?.let { fusedLocationClient.removeLocationUpdates(it) }
        locationCallback = null
    }

    fun isLocationPermissionGranted(): Boolean{
        return checkLocationPermission()
    }


}