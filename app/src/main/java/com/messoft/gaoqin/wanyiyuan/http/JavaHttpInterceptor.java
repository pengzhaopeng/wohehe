package com.messoft.gaoqin.wanyiyuan.http;

import android.content.Context;

import com.example.http.utils.CheckNetwork;
import com.messoft.gaoqin.wanyiyuan.utils.BusinessUtils;
import com.messoft.gaoqin.wanyiyuan.utils.InterfaceAuthenUtils;
import com.messoft.gaoqin.wanyiyuan.utils.StringUtils;
import com.messoft.gaoqin.wanyiyuan.utils.TimeUtils;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/11/13 0013.
 */

public class JavaHttpInterceptor implements Interceptor {

    private Context context;

    public JavaHttpInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request request;
        Request.Builder builder = null;

        //获取到第一个action
        String urlHost = originalRequest.url().host();
        //urlHost.equals(Constants.PHP_HOST_API
        String action = getUrlLastWord(originalRequest.url().toString());
        if (!StringUtils.isNoEmpty(action)) return null;
        if (StringUtils.isNoEmpty(BusinessUtils.getToken())) {
            //签名
            String data = null;
            if (originalRequest.body() instanceof FormBody) {
                FormBody oldFormBody = (FormBody) originalRequest.body();
                assert oldFormBody != null;
                int size = oldFormBody.size();
                for (int i = 0; i < size; i++) {
                    //拿到data的值
                    if (oldFormBody.name(i).equals("data")) {
                        //这里data直接getValue
                        data = oldFormBody.value(i);
//                        DebugUtil.debug("MyHttpHeadInterceptor", "data参数：" + data);
                    }
                }
                //要签名
                String mSign = InterfaceAuthenUtils.signTopRequestNew(
                        action,
                        data,
                        TimeUtils.getCurrentTime(),
                        BusinessUtils.getToken(),
                        BusinessUtils.getSecret());
                HttpUrl modifiedUrl = originalRequest.url().newBuilder()
                        // Provide your custom parameter here
                        .addQueryParameter("appKey", "")
                        .addQueryParameter("timestamp", TimeUtils.getCurrentTime())
                        .addQueryParameter("sign", mSign)
                        .addQueryParameter("token", BusinessUtils.getToken())
                        .build();
//                DebugUtil.debug("JavaHttpInterceptor", "签名参数：timestamp：" + TimeUtils.getCurrentTime() + "--sign" +
//                        mSign + "--token:" + BusinessUtils.getToken() + "--data:" + data);

                request = originalRequest.newBuilder().url(modifiedUrl).build();
                builder = request.newBuilder();
            }
        } else {
            //不签名
            builder = originalRequest.newBuilder();
        }

        assert builder != null;
        builder.addHeader("Accept", "application/json;versions=1");
        if (CheckNetwork.isNetworkConnected(context)) {
            int maxAge = 60;
            builder.addHeader("Cache-Control", "public, max-age=" + maxAge);
        } else {
            int maxStale = 60 * 60 * 24 * 28;
            builder.addHeader("Cache-Control", "public, only-if-cached, max-stale=" + maxStale);
        }
        return chain.proceed(builder.build());
    }

    /**
     * 拿到url的最后一个单词
     * http://192.168.0.87:8080/api/msg/getCode
     * 拿到getCode
     *
     * @param url
     * @return
     */
    public String getUrlLastWord(String url) {
        if (!StringUtils.isNoEmpty(url)) return null;
//        String[] sub_url_array = url.split("[/ : . - _ # %]");
//        int sid;
//        for (sid = 0; sid < sub_url_array.length; sid++) {
//            if (sub_url_array[sid].equals("")) continue;
//            String result = sub_url_array[sub_url_array.length - 1];
//            if (StringUtils.isNoEmpty(result)) {
//                return result;
//            }
//        }
//        return null;
        String[] sub_url_array = url.split("=");
        int length = sub_url_array.length;
        if (length < 1) {
            return null;
        }
        return sub_url_array[length - 1];
    }
}
