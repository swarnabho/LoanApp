package com.textifly.quickmudra.ApiManager;

import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.textifly.quickmudra.Utils.Urls;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(60*1000, TimeUnit.MILLISECONDS);
        httpClient.readTimeout(60*1000, TimeUnit.MILLISECONDS);
        httpClient.writeTimeout(60*1000, TimeUnit.MILLISECONDS);
        httpClient.addInterceptor(logging);
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(Urls.BASEURL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(httpClient.build())
                    .build();

        }
        return retrofit;
    }

}
