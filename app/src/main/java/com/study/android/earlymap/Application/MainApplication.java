package com.study.android.earlymap.Application;

import android.app.Application;
import android.util.Log;

import com.study.android.earlymap.Service.MapService;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by tatsuya on 2018/04/11.
 */

public class MainApplication extends Application {
    protected static MapService service;

    @Override
    public void onCreate() {
        super.onCreate();
        String url="https://maps.googleapis.com/";

            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Log.d("---", chain.request().url().toString());
                            return chain.proceed(chain.request());
                        }
                    });
            Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
            retrofitBuilder.baseUrl(url);
            retrofitBuilder.client(clientBuilder.build())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            service = retrofitBuilder.build().create(MapService.class);
        }

    public static MapService getService() {
        return service;
    }
}
