package fernandagallina.weatherapp.inject

import javax.inject.Singleton

import dagger.Component
import fernandagallina.weatherapp.App
import fernandagallina.weatherapp.MainActivity
import retrofit2.Retrofit

@Singleton
@Component(modules = arrayOf(AppModule::class, NetModule::class))
interface NetComponent {

    fun retrofit(): Retrofit

    fun inject(activity: MainActivity)

    fun inject(app: App)
}