package fernandagallina.weatherapp

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

import org.json.JSONObject

import android.content.Context
import android.util.Log

object RemoteFetch {
    private val GOOGLE_MAPS_API = "https://maps.googleapis.com/maps/api/timezone/json?location=%s,%s&timestamp=%s&key=AIzaSyB2VB0Ac0oh5YBOExQyH6GozQWLpaLSbsY"

    private val OPEN_WEATHER_MAP_API = "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric"

    fun getJSONGoogleMaps(lat: String, lon: String, dt: String): JSONObject? {

        var url: URL? = null
        try {
            url = URL(String.format(GOOGLE_MAPS_API, lat, lon, dt))

            val connection = url.openConnection() as HttpURLConnection

            val reader = BufferedReader(
                    InputStreamReader(connection.inputStream))

            val json = StringBuffer(1024)
            var tmp = reader.readLine()
            while ((tmp) != null)
                json.append(tmp).append("\n")
            reader.close()

            return JSONObject(json.toString())
        } catch (e: Exception) {
            return null
        }

    }

    fun getJSON(context: Context, city: String): JSONObject? {
        try {
            val url = URL(String.format(OPEN_WEATHER_MAP_API, city))
            val connection = url.openConnection() as HttpURLConnection

            connection.addRequestProperty("x-api-key",
                    context.getString(R.string.open_weather_maps_app_id))

            val reader = BufferedReader(
                    InputStreamReader(connection.inputStream))

            val json = StringBuffer(1024)
            var tmp = reader.readLine()
            while ((tmp) != null)
                json.append(tmp).append("\n")
            reader.close()

            val data = JSONObject(json.toString())

            // This value will be 404 if the request was not
            // successful
            return if (data.getInt("cod") != 200) {
                null
            } else data

        } catch (e: Exception) {
            return null
        }

    }
}