package com.xingjiezheng.plugindemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import mock.weaving.DebugLog;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helloWorld();

    }

    @DebugLog
    private void helloWorld() {
        Log.i(TAG, "hello world");

    }
}
