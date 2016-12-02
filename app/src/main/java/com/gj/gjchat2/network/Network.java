// (c)2016 Flipboard Inc, All Rights Reserved.

package com.gj.gjchat2.network;

import com.gj.gjlibrary.util.AbMd5;
import com.gj.gjlibrary.util.logger.AbLog;

import java.util.Arrays;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Network {

    private static Api api;
    private static OkHttpClient okHttpClient;
    private static HttpLoggingInterceptor httpLoggingInterceptor;
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();
    private static String BaseURL = "http://192.168.2.212:8006/";

    public static void setBaseURL(String urlStr){
        BaseURL = "http://" + urlStr + "/";
    }

    public static String getBaseURL(){
        return BaseURL;
    }


    public static Api getApi() {
        if (api == null) {

            httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    AbLog.i("httpLoggingInterceptor message: "+message);
                }
            });
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            //okHttpClient = new OkHttpClient();

            okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();

            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(BaseURL)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            api = retrofit.create(Api.class);
        }
        return api;
    }

    /**
     * 加密
     *
     * @return
     */
    public static String encrypt(String[] strs) {
        Arrays.sort(strs);
        String url = "";
        for (int i = 0; i < strs.length; i++) {
            if (i == strs.length - 1) {
                url += strs[i];
            } else {
                url += strs[i] + "&";
            }
        }
        url += "5cf253019b32380d31aacf55ac516241";
        String md5Url = AbMd5.MD5(url).toLowerCase().toString();
        return md5Url;
    }
}
