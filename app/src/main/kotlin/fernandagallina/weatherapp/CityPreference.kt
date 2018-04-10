package fernandagallina.weatherapp

import android.app.Activity
import android.content.SharedPreferences

class CityPreference (activity: Activity) {

    internal var prefs: SharedPreferences

    internal var city: String
        get() = prefs.getString("city", "Berlin")
        set(city) {
            prefs.edit().putString("city", city).commit()
        }

    init {
        prefs = activity.getPreferences(Activity.MODE_PRIVATE)
    }

}