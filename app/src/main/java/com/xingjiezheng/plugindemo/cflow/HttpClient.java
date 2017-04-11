package com.xingjiezheng.plugindemo.cflow;

import android.util.Log;

import java.net.URI;
import java.net.URISyntaxException;

import mock.weaving.MockUri;

/**
 * Created by XingjieZheng
 * on 2017/4/11.
 */

public class HttpClient {

    public static String serverUrl = "http://http://10.10.152.48:8070/";
    private static final String TAG = HttpClient.class.getSimpleName();

    @MockUri(host = "10.10.120.127", port = "9999")
    public URI createURI(String url) {
        URI uri = null;
        try {
            if (!url.startsWith("http://")) {
                url = serverUrl + url;
            }
            uri = new URI(url);
            Log.i(TAG, "url:" + url);
        } catch (URISyntaxException e) {
            Log.e(TAG, "URL格式不正确: " + url);
        }
        return uri;
    }

}
