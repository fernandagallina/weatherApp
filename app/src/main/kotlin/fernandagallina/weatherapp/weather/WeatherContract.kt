package fernandagallina.weatherapp.weather

import fernandagallina.weatherapp.model.WeatherInfo

interface WeatherContract {

    interface View {

        fun setWeatherIcon(weatherInfo: WeatherInfo)

        fun showErrorMessage(localizedMessage: String?)
    }

    interface ActionListener {

        fun loadWeatherData(city: String)
    }
}
