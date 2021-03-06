package com.messoft.gaoqin.wanyiyuan;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import okhttp3.OkHttpClient;

/**
* debug 不执行
* @author 鹏鹏鹏先森
* created at 2018/10/15 14:52
*/
public class StethoUtils {

    public static void initStetho(Application app){
        Stetho.initializeWithDefaults(app);
    }

    public static OkHttpClient.Builder addStathoNetWork(OkHttpClient.Builder builder){
        return builder.addNetworkInterceptor(new StethoInterceptor());
    }
}
