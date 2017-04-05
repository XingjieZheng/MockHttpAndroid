package com.xingjiezheng.plugindemo.http;

/**
 * Created by liukun on 16/3/10.
 */
public abstract class SubscriberListener<T> {
    public abstract void onNext(T response);
    public void onError(Throwable e){}
    public void onCompleted(){}
    public void onBegin(){}
}
