package fernandagallina.weatherapp.di

import android.app.Application

import javax.inject.Singleton

import dagger.Module
import dagger.Provides

@Module
class AppModule(private val app: Application) {

    @Provides
    @Singleton
    internal fun provideApplication(): Application {
        return app
    }
}
