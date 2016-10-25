package fernandagallina.weatherapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

public class RemoteFetch {
    private static final String GOOGLE_MAPS_API =
            "https://maps.googleapis.com/maps/api/timezone/json?location=%s,%s&timestamp=%s&key=AIzaSyB2VB0Ac0oh5YBOExQyH6GozQWLpaLSbsY";

    private static final String OPEN_WEATHER_MAP_API =
            "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric";

    @Nullable
    public static JSONObject getJSONGoogleMaps(String lat, String lon, String dt) {

        URL url = null;
        try {
            url = new URL(String.format(GOOGLE_MAPS_API, lat, lon, dt));

            HttpURLConnection connection =
                    (HttpURLConnection)url.openConnection();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp="";
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            return data;
        } catch(Exception e){
            return null;
        }
    }

    public static JSONObject getJSON(Context context, String city){
        try {
            URL url = new URL(String.format(OPEN_WEATHER_MAP_API, city));
            HttpURLConnection connection =
                    (HttpURLConnection)url.openConnection();

            connection.addRequestProperty("x-api-key",
                    context.getString(R.string.open_weather_maps_app_id));

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp="";
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            // This value will be 404 if the request was not
            // successful
            if(data.getInt("cod") != 200){
                return null;
            }

            return data;
        }catch(Exception e){
            return null;
        }
    }
}