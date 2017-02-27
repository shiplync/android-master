package com.traansmission.api;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.traansmission.shared.Auth;
import com.traansmission.shared.Constants;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by SAMBUCA on 3/7/16.
 */
public class RestClient {

    private static RestClient mInstance = null;
    private static final String BASE_URL = Constants.BASE_URL;
    Retrofit retrofit;
    Retrofit retrofitAuthenticated;

    private RestClient(final String token){
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        // For authorization
        Interceptor interceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder().addHeader("Authorization", "Token " + token).build();
                return chain.proceed(newRequest);
            }
        };
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.interceptors().add(interceptor);
        OkHttpClient client = builder.build();
        retrofitAuthenticated = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
    }

    public static RestClient getInstance(Context context) {
        if(mInstance == null)
        {
            mInstance = new RestClient(Auth.getToken(context));
        }

        return mInstance;
    }

    public void resetRestClient() {
        mInstance = null;
    }

    public Endpoints apiService() {
        return retrofit.create(Endpoints.class);
    }
    public Endpoints apiServiceAuthenticated() {
        return retrofitAuthenticated.create(Endpoints.class);
    }
}
