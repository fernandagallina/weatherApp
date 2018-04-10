package fernandagallina.weatherapp.weather

import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import fernandagallina.weatherapp.App
import fernandagallina.weatherapp.CityPreference
import fernandagallina.weatherapp.R
import fernandagallina.weatherapp.RemoteFetch
import fernandagallina.weatherapp.inject.DaggerWeatherComponent
import fernandagallina.weatherapp.inject.WeatherModule
import kotlinx.android.synthetic.main.fragment_main.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class WeatherFragment : Fragment(), WeatherContract.View {
    @Inject
    lateinit var presenter: WeatherPresenter

    lateinit var weatherFont: Typeface

    var handler: Handler = Handler(Looper.getMainLooper())
    var handlerTime: Handler = Handler(Looper.getMainLooper())
    var sunsetValue: Long = 0
    var sunriseValue: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getComponent()
        weatherFont = Typeface.createFromAsset(activity.assets, "weather.ttf")
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

    private fun setWeatherIcon(actualId: Int, sunrise: Long, sunset: Long) {

        val id = actualId / 100
        var icon = ""
        var color = R.color.black
        val res = resources
        if (actualId == 800) {
            val currentTime = Date().time
            if (currentTime >= sunrise && currentTime < sunset) {
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
    }

    private fun updateCityData(lat: String, lon: String, dt: String) {
        val runnable = Runnable {
            val json = RemoteFetch.getJSONGoogleMaps(lat, lon, dt)
            if (json == null) {
                handlerTime.post {
                    Toast.makeText(activity,
                            activity.getString(R.string.datetime_not_found),
                            Toast.LENGTH_LONG).show()
                }
            } else {
                while (!Thread.currentThread().isInterrupted) {
                    try {
                        handlerTime.post { renderTime(json) }
                        Thread.sleep(10000)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }
            }
        }

        Thread(runnable).start()
    }

    private fun renderTime(json: JSONObject?) {
        try {
            val status = json!!.getString("status")
            if (status == "OK") {
                val timeZoneId = json.getString("timeZoneId")

                val tz = TimeZone.getTimeZone(timeZoneId)
                val destFormat = SimpleDateFormat("HH:mm")
                destFormat.timeZone = tz
                val today = Date()
                val s = destFormat.format(today)
                val ss = destFormat.format(Date(sunsetValue))
                val sr = destFormat.format(Date(sunriseValue))
                timeFieldTextView.text = s
                sunriseFieldTextView.text = sr
                sunsetFieldTextView.text = ss
            }
        } catch (e: Exception) {
            Log.e("Google TimeZone API", "One or more fields not found in the JSON data")
        }

    }

//    fun changeCity(city: String) {
//        loadWeatherData(city)
//    }
}
