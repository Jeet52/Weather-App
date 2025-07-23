package com.example.weather

//Imports - Additional Features
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.example.weather.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


//Key: 0c3d19d18bf2330314528f7056676ec8
class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //  Log.d("MainActivity", "MainActivity loaded")
        setContentView(binding.root)
        fetchWeatherData("Chicago")
        searchCity()
    }


    private fun searchCity() {
        val searchView = binding.searchView
        //Search Bar Functionality
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    val trimmed = it.trim()

                    //Error Checking and Condition Matching
                    when {
                        //   ZIP code: exactly 5 digits
                        trimmed.matches(Regex("^\\d{5}$")) -> {
                            fetchCityFromZip(trimmed)
                        }

                        // City name: letters/spaces only, 4+ characters
                        trimmed.matches(Regex("^[a-zA-Z ]{4,}$")) -> {
                            fetchWeatherData(trimmed)
                            binding.city.text = trimmed
                        }

                        // Invalid input
                        else -> {
                            binding.city.text = getString(R.string.invalid_input_message)
                        }
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    //Utilizes API to get data
    private fun fetchWeatherData(cityName: String) {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            //base url (same for everyone)
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(ApiInterface::class.java)
        val response =
            //api key (unique to everyone)
            retrofit.getWeatherData(cityName, "37a8d368a305516b14b1c1c4f01d1d7f", "imperial")
        response.enqueue(object : Callback<WeatherApp> {
            override fun onResponse(call: Call<WeatherApp>, response: Response<WeatherApp>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    //Declaring variables via val
                    val temperature = responseBody.main.temp.toString()
                    val humidity = responseBody.main.humidity
                    val windSpeed = responseBody.wind.speed
                    val sunRise = responseBody.sys.sunrise.toLong()
                    val sunSet = responseBody.sys.sunset.toLong()
                    val pressure = responseBody.main.pressure
                    val condition = responseBody.weather.firstOrNull()?.main ?: "unknown"
                    val maxTemp = responseBody.main.temp_max
                    val minTemp = responseBody.main.temp_min

                    //Update the UI layout via binding
                    binding.temp.text = "$temperature °F"
                    binding.weather.text = condition
                    binding.max.text = "Max.Temp: $maxTemp°"
                    binding.min.text = "Min.Temp: $minTemp°"
                    binding.humidity.text = "$humidity %"
                    binding.windSpeed.text = "$windSpeed m/s"
                    binding.sunrise.text = "${time(sunRise)}"
                    binding.sunset.text = "${time(sunSet)}"
                    binding.seaLevel.text = "$pressure hPa"
                    binding.conditions.text = condition
                    binding.day.text = dayName(System.currentTimeMillis())
                    binding.date.text = date()
                    binding.city.text = "${responseBody.name}, ${responseBody.sys.country}"
                    //Log.d("TAG", "onResponse: $temperature")
                    changeImagesAccordingToWeatherConditions(condition)
                } else {
                    // City not found or API failed to return data
                    binding.city.text = getString(R.string.city_not_found_message)
                }
            }

            override fun onFailure(call: Call<WeatherApp>, t: Throwable) {
                //Log.e("TAG", "onFailure: ${t.localizedMessage}")
            }

        })

    }

    //Convert ZIP to City
    private fun fetchCityFromZip(zipCode: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.zippopotam.us/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ZipApi::class.java)

        val response = retrofit.getZipInfo(zipCode)

        response.enqueue(object : Callback<ZipResponse> {
            override fun onResponse(call: Call<ZipResponse>, response: Response<ZipResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val city = response.body()?.places?.get(0)?.placeName
                    val state = response.body()?.places?.get(0)?.state

                    //Error Checking -> Not Blank/Empty
                    if (!city.isNullOrBlank() && !state.isNullOrBlank()) {
                        // Show city + state
                        binding.city.text = "$city, $state"

                        //Use ZIP+country for weather
                        fetchWeatherData("$zipCode,us")
                    } else {
                        binding.city.text = getString(R.string.city_or_state_not_found)
                    }
                } else {
                    binding.city.text = getString(R.string.invalid_zip_code)
                }
            }

            override fun onFailure(call: Call<ZipResponse>, t: Throwable) {
                binding.city.text = getString(R.string.invalid_zip_or_city)
            }
        })
    }

    //Background depends on weather condition
    private fun changeImagesAccordingToWeatherConditions(conditionS: String) {
        val condition = conditionS.lowercase() // Normalize input to lowercase
        Log.d("WEATHER_CONDITION", "Condition from API: $condition")

        when (conditionS) {
            "Clear" -> {
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)
            }

            "Clouds" -> {
                binding.root.setBackgroundResource(R.drawable.colud_background)
                binding.lottieAnimationView.setAnimation(R.raw.cloud)
            }

            "Rain" -> {
                binding.root.setBackgroundResource(R.drawable.rain_background)
                binding.lottieAnimationView.setAnimation(R.raw.rain)
            }

            "Snow" -> {
                binding.root.setBackgroundResource(R.drawable.snow_background)
                binding.lottieAnimationView.setAnimation(R.raw.snow)
            }

            else -> {
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)
            }
        }
        binding.lottieAnimationView.playAnimation()
    }

    //Helper Functions for data, time, and day (current)
    private fun date(): String {
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format((Date()))
    }

    private fun time(timeStamp: Long): String {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return sdf.format((Date(timeStamp * 1000)))
    }


    fun dayName(timeStamp: Long): String {
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format((Date(timeStamp)))
    }
}
