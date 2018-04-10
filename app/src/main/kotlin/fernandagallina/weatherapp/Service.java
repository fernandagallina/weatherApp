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

    public Observable<WeatherInfo> getCityWeatherInfo(String city, String unit) {
        return service.getCityWeatherInfo(city, "a0b93aa575ee75ac1301db3a0a532b13"); //1dbeef573e364c40f0cf21cc6aad21de

    }

    public static <T> SingleTransformer<T, T> applySingleSchedulers() {
        return single -> single.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }

    public static <T> ObservableTransformer<T, T> applyObservableSchedulers() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }
}