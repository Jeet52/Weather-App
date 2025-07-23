package com.example.weather



import com.google.gson.annotations.SerializedName

data class ZipResponse(
    val places: List<Place>
)

data class Place(
    @SerializedName("place name")
    val placeName: String,
    val state: String
) {
}