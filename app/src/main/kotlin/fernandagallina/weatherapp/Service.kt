package fernandagallina.weatherapp

import fernandagallina.weatherapp.api.WeatherAPI
import fernandagallina.weatherapp.model.WeatherInfo
import io.reactivex.Single
import retrofit2.Retrofit

class Service(retrofit: Retrofit) {

    private val service: WeatherAPI = retrofit.create(WeatherAPI::class.java)

    fun getCityWeatherInfo(city: String): Single<WeatherInfo> {
        return service.getCityWeatherInfo(city, "metric", "a0b93aa575ee75ac1301db3a0a532b13")
        //1dbeef573e364c40f0cf21cc6aad21de
    }
}
