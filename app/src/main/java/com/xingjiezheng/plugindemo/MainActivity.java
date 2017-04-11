package com.xingjiezheng.plugindemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xingjiezheng.plugindemo.http.BaseSubscriber;
import com.xingjiezheng.plugindemo.http.HttpMethod;
import com.xingjiezheng.plugindemo.http.SubscriberListener;
import com.xingjiezheng.plugindemo.http.ZAResponse;
import com.xingjiezheng.plugindemo.http.ZARetrofit;
import com.xingjiezheng.plugindemo.login.LoginService;

import mock.weaving.DebugLog;
import mock.weaving.DebugMockRetrofit;
import retrofit2.Retrofit;
import rx.Observable;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.text);

        helloWorld();
        request();
    }

    @DebugLog
    private void helloWorld() {
        Log.i(TAG, "hello world");
    }

    @DebugMockRetrofit(host = "10.10.124.92", port = "9999")
    private Retrofit getRetrofit() {
        return ZARetrofit.getInstance().getRetrofit();
    }

    private void request() {
//        OkHttpClient client = new OkHttpClient.Builder()
//                .build();
//        Retrofit mRetrofit = new Retrofit.Builder()
//                .baseUrl("http://192.168.0.137:9999/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .client(client)
//                .build();
//        LoginService service = mRetrofit.create(LoginService.class);

        LoginService service = getRetrofit().create(LoginService.class);

        Observable<ZAResponse<Object>> observable = service.login();
        HttpMethod.toSubscribe(observable,
                new BaseSubscriber(new SubscriberListener<ZAResponse<Object>>() {

                    @Override
                    public void onNext(ZAResponse<Object> response) {
                        String result = new Gson().toJson(response);
                        Log.i(TAG, result);
                        textView.setText(result);
                    }
                }, this));

    }
}
