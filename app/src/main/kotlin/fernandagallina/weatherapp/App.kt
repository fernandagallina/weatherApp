package fernandagallina.weatherapp

import android.app.Application
import fernandagallina.weatherapp.inject.NetComponent
import fernandagallina.weatherapp.inject.NetModule
import fernandagallina.weatherapp.inject.AppModule
import fernandagallina.weatherapp.inject.DaggerNetComponent

class App : Application() {

    internal val API_BASE_URL = "http://api.openweathermap.org/"

    lateinit var netComponent: NetComponent

    override fun onCreate() {
        super.onCreate()
        netComponent = DaggerNetComponent.builder()
                .appModule(AppModule(this))
                .netModule(NetModule(API_BASE_URL))
                .build()
    }
}