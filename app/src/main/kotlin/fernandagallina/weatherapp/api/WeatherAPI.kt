package fernandagallina.weatherapp.api

import fernandagallina.weatherapp.model.WeatherInfo
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherAPI {
    @GET("/data/2.5/weather")
    fun getCityWeatherInfo(
            @Query("q") city: String,
//            @Query("units") units: String, // metric
            @Query("APPID") appId: String
    ): Observable<WeatherInfo>
}
