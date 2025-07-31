# 🌦️ Weather App (Android + Kotlin)

A clean, modern weather app built with **Kotlin** for Android. Enter a ZIP code to fetch and display real-time weather data using the **OpenWeatherMap API** — presented in a sleek, Samsung-inspired UI with **Lottie animations** and beautiful typography.

---

## ✨ Features

- 🔍 **ZIP code search** (via `SearchView`)
- 📡 **Real-time weather updates** using OpenWeatherMap API
- 🌈 **Custom UI** with modern design
- 🖋️ **Samsung-style font** for a minimalist aesthetic
- 🎞️ **Lottie animations** for dynamic weather visuals
- 🌤️ Displays:
  - Current temperature
  - Weather condition & icon
  - High/Low temp
  - Wind speed
  - Humidity
  - Sunrise/Sunset
  - Pressure
- 🧭 Locale-aware date and time formatting
- ⚙️ Built with **Retrofit**, **ViewBinding**, and **Material Design**

---

## 🛠️ Tech Stack

- **Kotlin**
- **Retrofit2** – for API networking
- **ViewBinding** – for type-safe view access
- **Lottie** – for smooth vector animations
- **Gson** – for JSON parsing
- **AppCompat / Material Components** – UI design
- **OpenWeatherMap API** – real-time weather data

---

## 📦 Installation & Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/Jeet52/Weather-App.git
   cd Weather-App
Open in Android Studio
Add your OpenWeatherMap API key
You can define it as a constant (or better, use BuildConfig):
const val API_KEY = "your_api_key"
Run on an emulator or real device

## 📂 Project Structure
Weather-App/
├── app/
│   └── src/
│       └── main/
│           ├── java/com/example/weather/
│           │   ├── MainActivity.kt
│           │   ├── ApiInterface.kt
│           │   ├── ZipApi.kt
│           │   ├── models/
│           │   │   ├── Coord.kt, Clouds.kt, Sys.kt, Weather.kt, Wind.kt
│           └── res/
│               ├── layout/activity_main.xml
│               ├── font/ (Samsung-style font)
│               ├── raw/ (Lottie .json animations)
│               └── values/
├── build.gradle
└── README.md

Jeet Patel
📍 University of Illinois at Chicago
🧠 Stats Major, CS & Econ Minor
🛠️ Data Science & Web Development Intern
🏆 Spark Hacks Hackathon Winner
🔗 [GitHub](https://github.com/Jeet52)
