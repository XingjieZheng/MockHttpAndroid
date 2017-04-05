package com.xingjiezheng.plugindemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;
import com.xingjiezheng.plugindemo.http.BaseSubscriber;
import com.xingjiezheng.plugindemo.http.HttpMethod;
import com.xingjiezheng.plugindemo.http.SubscriberListener;
import com.xingjiezheng.plugindemo.http.ZAResponse;
import com.xingjiezheng.plugindemo.http.ZARetrofit;
import com.xingjiezheng.plugindemo.login.LoginService;

import mock.weaving.DebugMockRetrofit;
import retrofit2.Retrofit;
import rx.Observable;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helloWorld();
        mockHttp();

        request();
    }

    //    @DebugLog
    private void helloWorld() {
        Log.i(TAG, "hello world");

    }

    //    @DebugHttpMock
    private void mockHttp() {
        Log.i(TAG, "mock http");
    }

    @DebugMockRetrofit(host = "10.10.124.92", port = "9999")
    private Retrofit getRetrofit() {
        return ZARetrofit.getInstance().getRetrofit();
    }

    private void request() {

        LoginService service = getRetrofit().create(LoginService.class);

//        OkHttpClient client = new OkHttpClient.Builder()
//                .build();
//        Retrofit mRetrofit = new Retrofit.Builder()
//                .baseUrl("http://192.168.0.137:9999/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .client(client)
//                .build();
//        LoginService service = mRetrofit.create(LoginService.class);


        Observable<ZAResponse<String>> observable = service.login("40756205", "111111", 1, true, "902684", "3", "3.8.1");
        HttpMethod.toSubscribe(observable,
                new BaseSubscriber(new SubscriberListener<ZAResponse<String>>() {

                    @Override
                    public void onNext(ZAResponse<String> response) {
                        Log.i(TAG, new Gson().toJson(response));
                    }
                }, this));
    }
}
