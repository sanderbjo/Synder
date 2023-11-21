package com.example.synder.utilities

import com.example.synder.models.Coordinates
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlin.math.roundToInt



@Module
@InstallIn(SingletonComponent::class)
object GeographicalUtils {
    private const val EARTH_RADIUS_KM = 6371.0
    @Provides
    fun provideGeographicalUtils(): GeographicalUtils = GeographicalUtils


    fun calculateDistance(coord1: Coordinates, coord2: Coordinates): Int {
        val lat1 = Math.toRadians(coord1.latitude)
        val lon1 = Math.toRadians(coord1.longitude)
        val lat2 = Math.toRadians(coord2.latitude)
        val lon2 = Math.toRadians(coord2.longitude)

        val dlat = lat2 - lat1
        val dlon = lon2 - lon1

        val a = sin(dlat / 2).pow(2) + cos(lat1) * cos(lat2) * sin(dlon / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        val distanceInKilometers = EARTH_RADIUS_KM * c
        return distanceInKilometers.roundToInt()
    }
}