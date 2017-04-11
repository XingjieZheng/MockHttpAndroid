package com.xingjiezheng.plugindemo.cflow;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.xingjiezheng.plugindemo.R;

import mock.weaving.DebugLog;
import mock.weaving.MockUriRequest;

public class CflowTestActivity extends AppCompatActivity {

    private TextView textView;
    private static final String TAG = CflowTestActivity.class.getSimpleName();
    private Button btn;

    private HttpClient httpClient = new HttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cfow_test);
        textView = (TextView) findViewById(R.id.text);
        btn = (Button) findViewById(R.id.btn);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                cfowTest();
//            }
//        });
        cflowTest();
    }

    private void cflowTest() {
        Log.i(TAG, "cflow Test");
        httpClient.createURI("other.do");

        request();

        httpClient.createURI("other2.do");
    }

    @MockUriRequest()
    private void request() {
        httpClient.createURI("request.do");
    }

}
