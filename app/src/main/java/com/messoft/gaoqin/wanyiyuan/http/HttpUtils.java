package com.messoft.gaoqin.wanyiyuan.http;

import android.content.Context;

import com.example.http.HttpHead;
import com.example.http.IpmlTokenGetListener;
import com.example.http.NullOnEmptyConverterFactory;
import com.example.http.ParamNames;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.messoft.gaoqin.wanyiyuan.app.Constants;

import java.lang.reflect.Field;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络请求工具类
 */

public class HttpUtils {
    private static HttpUtils instance;
    private Gson gson;
    private Context context;
    private Object javaWYYHttps;
    private Object javaMemberHttps;
    private Object javaCommonHttps;
    private Object javaQgHttps;

    private IpmlTokenGetListener listener;
    private boolean debug; //正式包关闭
    /**
     * 分页数据，每页的数量
     */
    public static int per_page = 10;
    public static int per_page_20 = 20;
//    public static int start_page = 1; //php分页1开始
    public static int start_page_java = 0;//java 分页0开始 麻痹
//    public static int per_page_more = 20;

    public static HttpUtils getInstance() {
        if (instance == null) {
            synchronized (HttpUtils.class) {
                if (instance == null) {
                    instance = new HttpUtils();
                }
            }
        }
        return instance;
    }

    public void init(Context context, boolean debug) {
        this.context = context;
        this.debug = debug;
        HttpHead.init(context);
    }


    public <T> T getJavaWYYServer(Class<T> a) {
        if (javaWYYHttps == null) {
            synchronized (HttpUtils.class) {
                if (javaWYYHttps == null) {
                    javaWYYHttps = getBuilder(Constants.URL_WYY).build().create(a);
                }
            }
        }
        return (T) javaWYYHttps;
    }

    public <T> T getJavaMemberServer(Class<T> a) {
        if (javaMemberHttps == null) {
            synchronized (HttpUtils.class) {
                if (javaMemberHttps == null) {
                    javaMemberHttps = getBuilder(Constants.URL_MEMBER).build().create(a);
                }
            }
        }
        return (T) javaMemberHttps;
    }

    public <T> T getJavaCommonServer(Class<T> a) {
        if (javaCommonHttps == null) {
            synchronized (HttpUtils.class) {
                if (javaCommonHttps == null) {
                    javaCommonHttps = getBuilder(Constants.URL_COMMON).build().create(a);
                }
            }
        }
        return (T) javaCommonHttps;
    }

    public <T> T getJavaQgServer(Class<T> a) {
        if (javaQgHttps == null) {
            synchronized (HttpUtils.class) {
                if (javaQgHttps == null) {
                    javaQgHttps = getBuilder(Constants.URL_QG).build().create(a);
                }
            }
        }
        return (T) javaQgHttps;
    }



    ///////////////////////////////问答模块////////////////////////////////////////

    private Retrofit.Builder getBuilder(String apiUrl) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.client(getOkClient());
        builder.baseUrl(apiUrl);//设置远程地址
        builder.addConverterFactory(new NullOnEmptyConverterFactory());
        builder.addConverterFactory(GsonConverterFactory.create(getGson()));
        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        return builder;
    }


    private Gson getGson() {
        if (gson == null) {
            GsonBuilder builder = new GsonBuilder();
            builder.setLenient();
            builder.setFieldNamingStrategy(new AnnotateNaming());
            builder.serializeNulls();
            gson = builder.create();
        }
        return gson;
    }


    private static class AnnotateNaming implements FieldNamingStrategy {
        @Override
        public String translateName(Field field) {
            ParamNames a = field.getAnnotation(ParamNames.class);
            return a != null ? a.value() : FieldNamingPolicy.IDENTITY.translateName(field);
        }
    }

    public OkHttpClient getUnsafeOkHttpClient() {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[]{};
                }
            }};
            // Install the all-trusting trust manager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            OkHttpClient.Builder okBuilder = new OkHttpClient.Builder()
                    .readTimeout(20, TimeUnit.SECONDS)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .addInterceptor(new JavaHttpInterceptor(context))
                    .addInterceptor(getInterceptor())
                    .sslSocketFactory(sslSocketFactory);

//            okBuilder = StethoUtils.addStathoNetWork(okBuilder);//添加stetho release包剔除
            okBuilder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
//                    Log.d("HttpUtils", "==come");
                    return true;
                }
            });
            return okBuilder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public OkHttpClient getOkClient() {
        OkHttpClient client1;
        client1 = getUnsafeOkHttpClient();
        return client1;
    }

    public void setTokenListener(IpmlTokenGetListener listener) {
        this.listener = listener;
    }


    private HttpLoggingInterceptor getInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        if (debug) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY); // 测试
        } else {
            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE); // 打包
        }
        return interceptor;
    }
}
