package com.xingjiezheng.plugindemo.http;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * @author chenzijin
 * @version 1.0.0
 * @date 2016/6/15
 */
public class HttpMethod {
    public static <T> void toSubscribe(Observable<T> o, final BaseSubscriber<T> s) {
        o.subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        s.onBegin();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }
}
