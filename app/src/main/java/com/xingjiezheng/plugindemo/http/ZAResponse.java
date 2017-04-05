package com.xingjiezheng.plugindemo.http;


/**
 * @author chenzijin
 * @version 1.0.0
 * @date 2016/5/18
 */
public class ZAResponse<T> extends Entity {
    public T data;
    public String msg;
    public int status;
    public String newStatus;

    @Override
    public String[] uniqueKey() {
        return new String[0];
    }
}
