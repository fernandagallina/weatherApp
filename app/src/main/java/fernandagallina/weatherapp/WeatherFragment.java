package fernandagallina.weatherapp;

import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * A placeholder fragment containing a simple view.
 */
public class WeatherFragment extends Fragment {
    Typeface weatherFont;

    TextView cityField;
//    TextView updatedField;
    TextView detailsField;
    TextView currentTemperatureField;
    TextView weatherIcon;
    TextView timeField;

    View rootView;

    Handler handler;
    Handler handlerTime;

    String lat, lon, dt;

    public WeatherFragment(){
        handler = new Handler(Looper.getMainLooper());
        handlerTime = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weatherFont = Typeface.createFromAsset(getActivity().getAssets(), "weather.ttf");
        updateWeatherData(new CityPreference(getActivity()).getCity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        cityField = (TextView)rootView.findViewById(R.id.city_field);
//        updatedField = (TextView)rootView.findViewById(R.id.updated_field);
        detailsField = (TextView)rootView.findViewById(R.id.details_field);
        currentTemperatureField = (TextView)rootView.findViewById(R.id.current_temperature_field);
        weatherIcon = (TextView)rootView.findViewById(R.id.weather_icon);
        timeField = (TextView)rootView.findViewById(R.id.time_field);

        weatherIcon.setTypeface(weatherFont);
        return rootView;
    }

    private void updateWeatherData(final String city){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final JSONObject json = RemoteFetch.getJSON(getActivity(), city);
                if(json == null){
                    handler.post(new Runnable(){
                        public void run(){
                            Toast.makeText(getActivity(),
                                    getActivity().getString(R.string.place_not_found),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable(){
                        public void run(){
                            renderWeather(json);
                        }
                    });
                }
            }
        };

        new Thread(runnable).start();
    }


    private void renderWeather(JSONObject json){
        try {

            cityField.setText(json.getString("name").toUpperCase(Locale.US) +
                    ", " +
                    json.getJSONObject("sys").getString("country"));

            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            JSONObject main = json.getJSONObject("main");
            JSONObject coord = json.getJSONObject("coord");

            detailsField.setText(
                    details.getString("description").toUpperCase(Locale.US) +
                            "\n" + "Humidity: " + main.getString("humidity") + "%" +
                            "\n" + "Pressure: " + main.getString("pressure") + " hPa");

            currentTemperatureField.setText(
                    String.format("%.2f", main.getDouble("temp"))+ " ℃");

//            DateFormat df = DateFormat.getDateTimeInstance();
//            String updatedOn = df.format(new Date(json.getLong("dt")*1000));
            dt = ""+ json.getLong("dt");
            lat = "" + coord.getLong("lat");
            lon = "" + coord.getLong("lon");
//            updatedField.setText("Last update: " + updatedOn);

            setWeatherIcon(details.getInt("id"),
                    json.getJSONObject("sys").getLong("sunrise") * 1000,
                    json.getJSONObject("sys").getLong("sunset") * 1000);

            updateCityData();

        }catch(Exception e){
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
        }
    }

    private void setWeatherIcon(int actualId, long sunrise, long sunset){
        int id = actualId / 100;
        String icon = "";
        if(actualId == 800){
            long currentTime = new Date().getTime();
            if(currentTime>=sunrise && currentTime<sunset) {
                icon = getActivity().getString(R.string.weather_sunny);
            } else {
                icon = getActivity().getString(R.string.weather_clear_night);
            }
        } else {
            switch(id) {
                case 2 : icon = getActivity().getString(R.string.weather_thunder);
                    break;
                case 3 : icon = getActivity().getString(R.string.weather_drizzle);
                    break;
                case 5 : icon = getActivity().getString(R.string.weather_rainy);
                    break;
                case 6 : icon = getActivity().getString(R.string.weather_snowy);
                    break;
                case 7 : icon = getActivity().getString(R.string.weather_foggy);
                    break;
                case 8 : icon = getActivity().getString(R.string.weather_cloudy);
                    break;
            }
        }
        weatherIcon.setText(icon);
    }

    private void updateCityData() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final JSONObject json = RemoteFetch.getJSONGoogleMaps(lat, lon, dt);
                if(json == null){
                    handlerTime.post(new Runnable(){
                        public void run(){
                            Toast.makeText(getActivity(),
                                    getActivity().getString(R.string.datetime_not_found),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    while (Thread.currentThread().isAlive()) {
                        try {
                            handlerTime.post(new Runnable(){
                                @Override
                                public void run(){
                                    renderTime(json);
                                }
                            });
                            Thread.sleep(60000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };

        new Thread(runnable).start();
    }


    private void renderTime(JSONObject json){
        try {
            String status = json.getString("status");
            if(status.equals("OK")) {
                String timeZoneId = json.getString("timeZoneId");

                TimeZone tz = TimeZone.getTimeZone(timeZoneId);
                SimpleDateFormat destFormat = new SimpleDateFormat("HH:mm");
                destFormat.setTimeZone(tz);
                Date today = new Date();
                String s = destFormat.format(today);
                timeField.setText(s);
            }
        }catch(Exception e){
            Log.e("Google TimeZone API", "One or more fields not found in the JSON data");
        }
    }

    public void changeCity(String city){
        updateWeatherData(city);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rootView = null; // cleaning up!
    }
}