package fernandagallina.weatherapp;

import fernandagallina.weatherapp.api.WeatherAPI;
import fernandagallina.weatherapp.model.WeatherInfo;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.Single;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class Service {

    private WeatherAPI service;

    public Service(Retrofit retrofit) {
        this.service = retrofit.create(WeatherAPI.class);
    }

    public Single<WeatherInfo> getCityWeatherInfo(String city) {
        return service.getCityWeatherInfo(city, "a0b93aa575ee75ac1301db3a0a532b13"); //1dbeef573e364c40f0cf21cc6aad21de
    }
}