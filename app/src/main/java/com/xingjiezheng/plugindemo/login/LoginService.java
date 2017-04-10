package com.xingjiezheng.plugindemo.login;

import com.xingjiezheng.plugindemo.http.ZAResponse;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface LoginService {

    @FormUrlEncoded
    @POST("login/login.do")
    Observable<ZAResponse<Object>> login();

}
