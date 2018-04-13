package fernandagallina.weatherapp.weather

import fernandagallina.weatherapp.Service
import fernandagallina.weatherapp.model.WeatherInfo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import javax.inject.Inject

class WeatherPresenter @Inject constructor(retrofit: Retrofit, private var view: WeatherContract.View) : WeatherContract.ActionListener {

    private var service: Service = Service(retrofit)

    override fun loadWeatherData(city: String) {
        val cityMetric = String.format("%s,%s", city, "metric");
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
        view.showErrorMessage(throwable.localizedMessage)
    }
}
