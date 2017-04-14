package com.xingjiezheng.plugindemo.http;

import com.xingjiezheng.plugindemo.BuildConfig;

import java.util.concurrent.TimeUnit;

import mock.weaving.MockRetrofitPartGetUri;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * @author chenzijin
 * @version 1.0.0
 * @date 2016/5/24
 */
public class ZARetrofit {
    private static String mServerAddress = "http://10.10.10.161";
    private static ZARetrofit instance;
    private static final int DEFAULT_TIMEOUT = 15;
    private Retrofit mRetrofit;

    private ZARetrofit() {
        initRetrofit();
    }

    private void initRetrofit() {
        initRetrofit(mServerAddress);
    }

    private void initRetrofit(String host) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            logging.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(host)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();
    }

    public static ZARetrofit getInstance() {
        if (instance == null) {
            synchronized (ZARetrofit.class) {
                if (instance == null) {
                    instance = new ZARetrofit();
                }
            }
        }
        return instance;
    }

    @MockRetrofitPartGetUri(host = "10.10.124.92", port = "9999")
    public Retrofit getRetrofit() {
        return mRetrofit;
    }

    public <T> T create(Class<T> service) {
        return mRetrofit.create(service);
    }

    public static <T> T getService(Class<T> service) {
        return ZARetrofit.getInstance().create(service);
    }

}
