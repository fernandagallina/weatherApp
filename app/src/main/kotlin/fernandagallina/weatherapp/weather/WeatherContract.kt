package fernandagallina.weatherapp.weather

interface WeatherContract {

    interface View

    interface ActionListener {
        fun loadWeatherData(city: String)
    }
}
