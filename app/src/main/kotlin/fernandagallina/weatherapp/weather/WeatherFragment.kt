package fernandagallina.weatherapp.weather

import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.text.InputType
import android.view.*
import android.widget.EditText
import android.widget.Toast
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
        setHasOptionsMenu(true)
        getComponent()
        presenter.loadWeatherData(CityPreference(activity).city)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_main, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.menu_main, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.change_city -> {
                showInputDialog()
                return true
            }

            R.id.celsius_fahr -> {
                changeUnit()
                return true
            }

            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

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
                color = R.color.black
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
                    color = R.color.black
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
        sunriseFieldTextView.text = destFormat.format(Date(weatherInfo.sys.sunrise * 1000))
        sunsetFieldTextView.text = destFormat.format(Date(weatherInfo.sys.sunset * 1000))
        currentTemperatureField.text = weatherInfo.main.temp.toString() + "°C"
        cityFieldTextView.text = weatherInfo.name.toUpperCase(Locale.ENGLISH) + ", " + weatherInfo.sys.country
        detailsFieldTextView.text = weatherInfo.weather[0].description + "\n" +
                "Humidity: " + weatherInfo.main.humidity.toString() + "% \n" +
                "Pressure: " + weatherInfo.main.pressure.toString() + " hPa"
    }

    override fun showErrorMessage(localizedMessage: String?) {
        Toast.makeText(context, localizedMessage, Toast.LENGTH_LONG).show()
    }

    private fun getComponent() {
        DaggerWeatherComponent.builder()
                .netComponent((activity.applicationContext as App).netComponent)
                .weatherModule(WeatherModule(this))
                .build().inject(this)
    }

    private fun showInputDialog() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Change city")
        val input = EditText(context)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)
        builder.setPositiveButton("Search") { _, _ -> changeCity(input.text.toString()) }
        builder.show()
    }

    fun changeCity(city: String) {
        presenter.loadWeatherData(city)
    }

    private fun changeUnit() {
        // todo
    }
}
