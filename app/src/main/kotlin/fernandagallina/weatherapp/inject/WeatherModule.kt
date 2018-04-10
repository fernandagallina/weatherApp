package fernandagallina.weatherapp.inject

import dagger.Module
import dagger.Provides
import fernandagallina.weatherapp.weather.WeatherContract
import fernandagallina.weatherapp.weather.WeatherPresenter
import retrofit2.Retrofit

@Module
class WeatherModule(var view: WeatherContract.View) {
    fun WeatherModule(view: WeatherContract.View) {
        this.view = view
    }

    @Provides
    @CustomScope
    fun providesWeatherPresenter(retrofit: Retrofit): WeatherPresenter = WeatherPresenter(retrofit, view)
}
