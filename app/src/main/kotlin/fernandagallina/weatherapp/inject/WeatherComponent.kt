package fernandagallina.weatherapp.inject

import dagger.Component
import fernandagallina.weatherapp.weather.WeatherFragment

@CustomScope
@Component(dependencies = arrayOf(NetComponent::class), modules = arrayOf(WeatherModule::class))
interface WeatherComponent {
    fun inject(activity: WeatherFragment)
}
