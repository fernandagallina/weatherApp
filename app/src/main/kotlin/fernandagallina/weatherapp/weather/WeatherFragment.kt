package fernandagallina.weatherapp.weather

import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fernandagallina.weatherapp.App
import fernandagallina.weatherapp.CityPreference
import fernandagallina.weatherapp.R
import fernandagallina.weatherapp.di.DaggerWeatherComponent
import fernandagallina.weatherapp.di.WeatherModule
import fernandagallina.weatherapp.model.WeatherInfo
import kotlinx.android.synthetic.main.fragment_main.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class WeatherFragment : Fragment(), WeatherContract.View {

    @Inject
    lateinit var presenter: WeatherPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getComponent()
        presenter.loadWeatherData(CityPreference(activity).city)
    }

    private fun getComponent() {
        DaggerWeatherComponent.builder()
                .netComponent((activity.applicationContext as App).netComponent)
                .weatherModule(WeatherModule(this))
                .build().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_main, container, false)
    }

//    private fun renderWeather(json: JSONObject?) {
//        try {
//
//            cityFieldTextView.text = json!!.getString("name").toUpperCase(Locale.US)
//
//            val details = json.getJSONArray("weather").getJSONObject(0)
//            val main = json.getJSONObject("main")
//            val coord = json.getJSONObject("coord")
//
//            detailsFieldTextView.text = details.getString("description").toUpperCase(Locale.US) +
//                    "\n" + "Humidity: " + main.getString("humidity") + "%" +
//                    "\n" + "Pressure: " + main.getString("pressure") + " hPa"
//
//            currentTemperatureField.text = String.format("%.2f", main.getDouble("temp")) + " ℃"
//
//            val dt = "" + json.getLong("dt")
//            val lat = "" + coord.getDouble("lat")
//            val lon = "" + coord.getDouble("lon")
//            sunriseValue = json.getJSONObject("sys").getLong("sunrise") * 1000
//            sunsetValue = json.getJSONObject("sys").getLong("sunset") * 1000
//            weatherIconTextView.typeface = weatherFont
//
//            setWeatherIcon(details.getInt("id"), sunriseValue, sunsetValue)
//
//            updateCityData(dt, lat, lon)
//
//        } catch (e: Exception) {
//            Log.e("SimpleWeather", "One or more fields not found in the JSON data")
//        }
//
//    }

    override fun setWeatherIcon(weatherInfo: WeatherInfo) {
       weatherIconTextView.typeface = Typeface.createFromAsset(activity.assets, "weather.ttf")

        val id = weatherInfo.weather[0].id / 100
        var icon = ""
        var color = R.color.black
        if (id == 8) {
            val currentTime = Date().time
            if (currentTime in weatherInfo.sys.sunrise..(weatherInfo.sys.sunset - 1)) {
                icon = activity.getString(R.string.weather_sunny)
                color = R.color.yellow
            } else {
                icon = activity.getString(R.string.weather_clear_night)
                color = R.color.white
            }
        } else {
            when (id) {
                2 -> {
                    icon = activity.getString(R.string.weather_thunder)
                    color = R.color.gray
                }
                3 -> icon = activity.getString(R.string.weather_drizzle)
                5 -> icon = activity.getString(R.string.weather_rainy)
                6 -> {
                    icon = activity.getString(R.string.weather_snowy)
                    color = R.color.white
                }
                7 -> {
                    icon = activity.getString(R.string.weather_foggy)
                    color = R.color.gray
                }
                8 -> {
                    icon = activity.getString(R.string.weather_cloudy)
                    color = R.color.gray
                }
            }
        }
        weatherIconTextView.text = icon
        weatherIconTextView.setTextColor(resources.getColor(color))

        val destFormat = SimpleDateFormat("HH:mm", Locale("en", "US"))
        val today = Date()
        timeFieldTextView.text = destFormat.format(today)
        sunriseFieldTextView.text = destFormat.format(Date(weatherInfo.sys.sunrise))
        sunsetFieldTextView.text = destFormat.format(Date(weatherInfo.sys.sunset))
        currentTemperatureField.text = weatherInfo.main.temp.toString()
    }
}
