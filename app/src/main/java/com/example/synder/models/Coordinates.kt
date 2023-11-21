package com.example.synder.models

data class Coordinates(
    val latitude: Double,
    val longitude: Double,
){
    constructor() : this(0.0,0.0)
}