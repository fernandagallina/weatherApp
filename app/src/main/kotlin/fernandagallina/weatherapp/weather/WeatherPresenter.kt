package fernandagallina.weatherapp.weather

import fernandagallina.weatherapp.Service
import fernandagallina.weatherapp.model.WeatherInfo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import javax.inject.Inject

class WeatherPresenter @Inject constructor(retrofit: Retrofit, view: WeatherContract.View) : WeatherContract.ActionListener {

    private var view: WeatherContract.View
    private var service: Service

    init {
        this.view = view
        service = Service(retrofit)
    }

    override fun loadWeatherData(city: String) {
        var cityMetric = String.format("%s,%s", city, "metric");
        service.getCityWeatherInfo(cityMetric)
                .compose({ single ->
                    single.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .unsubscribeOn(Schedulers.io())
                })
                .subscribe({ it -> this.onGetCityWeather(it) }, { throwable -> onError(throwable) })
    }

    private fun onGetCityWeather(weatherInfo: WeatherInfo) {
        view.setWeatherIcon(weatherInfo)
    }

    private fun onError(throwable: Throwable) {
        TODO("Show correct error message")
    }
}
