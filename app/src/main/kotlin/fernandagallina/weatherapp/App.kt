package fernandagallina.weatherapp

import android.app.Application
import fernandagallina.weatherapp.di.NetComponent
import fernandagallina.weatherapp.di.NetModule
import fernandagallina.weatherapp.di.AppModule
import fernandagallina.weatherapp.di.DaggerNetComponent

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