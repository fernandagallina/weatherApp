package fernandagallina.weatherapp.model

class WeatherInfo(val dt: String, val name: String, val coord: Coord, val main: Main, val sys: Sys, val weather: List<Weather>)

class Coord(val lat: String, val lon: String)

class Main(val temp: Float, val pressure: Float, val humidity: Float, val temp_min: Float, val temp_max: Float)

class Sys(val type: Int, val id: Int, val message: Float, val country: String, val sunset: Long, val sunrise: Long)

class Weather(val icon: String, val id: Int, val main: String, val description: String)

