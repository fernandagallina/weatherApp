package fernandagallina.weatherapp.weather

import fernandagallina.weatherapp.Service
import fernandagallina.weatherapp.model.Coord
import fernandagallina.weatherapp.model.WeatherInfo
import io.reactivex.functions.Consumer
import retrofit2.Retrofit
import javax.inject.Inject

class WeatherPresenter(retrofit: Retrofit, view: WeatherContract.View) : WeatherContract.ActionListener {

    private var view: WeatherContract.View
    private var service: Service

    init {
        this.view = view
        service = Service(retrofit)
    }

    @Inject
    fun WeatherPresenter(retrofit: Retrofit, view: WeatherContract.View) {
        this.view = view
        service = Service(retrofit)
    }

    override fun loadWeatherData(city: String) {
        service.getCityWeatherInfo(city, "metric")
                .compose(Service.applyObservableSchedulers())
                .subscribe({ it -> this.onGetCityWeather(it) }, { throwable -> onError(throwable) })
    }

    private fun onGetCityWeather(weatherInfo: WeatherInfo) {
        var coord = weatherInfo.coord
//        return
    }

    private fun onError(throwable: Throwable) {
        var string = throwable.message
    }
}
