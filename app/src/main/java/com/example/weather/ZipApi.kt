package com.example.weather

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ZipApi {
    @GET("us/{zip}")
    fun getZipInfo(@Path("zip") zip: String): Call<ZipResponse>
}