package com.xingjiezheng.plugindemo.http;

import android.content.Context;


import com.xingjiezheng.plugindemo.util.ToastUtils;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

public class BaseSubscriber<T> extends Subscriber<T> {

    private SubscriberListener mSubscriberOnNextListener;

    private Context context;
    private boolean isRetry;

    public BaseSubscriber(SubscriberListener mSubscriberOnNextListener, Context context) {
        this.mSubscriberOnNextListener = mSubscriberOnNextListener;
        this.context = context;
    }


    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    public void onBegin() {
        if (mSubscriberOnNextListener != null) {
            mSubscriberOnNextListener.onBegin();
        }
    }

    /**
     * 完成，隐藏ProgressDialog
     */
    @Override
    public void onCompleted() {
//        Toast.makeText(context, "Get Top Movie Completed", Toast.LENGTH_SHORT).show();
        if (mSubscriberOnNextListener != null) {
            mSubscriberOnNextListener.onCompleted();
        }
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }

    /**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     *
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (e instanceof UnknownHostException || e instanceof SocketException || e instanceof HttpException) {
            ToastUtils.showToast(context, "网络好像出问题了哦");
        } else if (e instanceof SocketTimeoutException) {
            ToastUtils.showToast(context, "网络好像出问题了哦");
        } else {
            ToastUtils.showToast(context, "网络好像出问题了哦");
        }
        if (mSubscriberOnNextListener != null) {
            mSubscriberOnNextListener.onError(e);
        }
    }

    /**
     * 将onNext方法中的返回结果交给Activity或Fragment自己处理
     *
     * @param response 创建Subscriber时的泛型类型
     */
    @Override
    public void onNext(T response) {
        if (mSubscriberOnNextListener != null) {
            mSubscriberOnNextListener.onNext(response);
        }
    }

    public void setRetry(boolean isRetry) {
        this.isRetry = isRetry;
    }

    public boolean isRetry() {
        return this.isRetry;
    }
}