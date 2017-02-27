package com.traansmission.shared;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.traansmission.api.Endpoints;
import com.traansmission.api.RestClient;
import com.traansmission.models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by SAMBUCA on 3/8/16.
 */
public class Initializers {
    private static Initializers mInstance = null;

    private Initializers(){

    }

    public static Initializers getInstance(){
        if(mInstance == null)
        {
            mInstance = new Initializers();
        }
        return mInstance;
    }


    public interface InitVersionResponse {
        void hasFinishedWithResponse(boolean versionOK);
        void hasFailed(Call<JsonElement> call, Throwable throwable, Response<JsonElement> response);

    }
    public static void initVersionCheck(Context context, final InitVersionResponse responseCallback) {
        Endpoints apiService = RestClient.getInstance(context).apiService();
        PackageInfo mPackageInfo;
        try {
            PackageManager packageManager = context.getPackageManager();
            mPackageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            mPackageInfo = null;
        }
        if(mPackageInfo==null) {responseCallback.hasFinishedWithResponse(true); return;}
        final Integer version = mPackageInfo.versionCode;
        Call<JsonElement> call = apiService.getCheckVersion(version);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.code()==200) {
                    JsonObject jsonObject = response.body().getAsJsonObject();
                    boolean verified = jsonObject.get("version_ok").getAsBoolean();
                    responseCallback.hasFinishedWithResponse(verified);
                } else {
                    responseCallback.hasFailed(call, null, response);
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                responseCallback.hasFailed(call, t, null);
            }
        });
    }

    public interface InitTokenResponse {
        void hasFinishedWithResponse(boolean tokenOK);
        void hasFailed(Call<JsonObject> call, Throwable throwable, Response<JsonObject> response);

    }
    public static void initTokenCheck(Context context, final InitTokenResponse responseCallback) {
        Endpoints apiService = RestClient.getInstance(context).apiServiceAuthenticated();
        String token = Auth.getToken(context);

        if(token != null) {
            Call<JsonObject> call = apiService.getVerifyToken();
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if(response.code()==200) {
                        JsonObject jsonObject = response.body().getAsJsonObject();
                        boolean tokenOK= jsonObject.get("token_ok").getAsBoolean();
                        responseCallback.hasFinishedWithResponse(tokenOK);
                    } else {
                        responseCallback.hasFailed(call, null, response);
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    responseCallback.hasFailed(call, t, null);
                }
            });
        } else {
            responseCallback.hasFinishedWithResponse(false);
        }
    }

    public interface InitUserResponse {
        void hasFinishedWithResponse(User user);
        void hasFailed(Call<User> call, Throwable throwable, Response<User> response);

    }
    public static void initUser(final Context context, final InitUserResponse responseCallback) {
        Endpoints apiService = RestClient.getInstance(context).apiServiceAuthenticated();


        Call<User> call = apiService.getUserSelf();
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code()==200) {
                    CurrentUser.getInstance().setCurrentUser(response.body());
                    responseCallback.hasFinishedWithResponse(response.body());
                } else {
                    responseCallback.hasFailed(call, null, response);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                responseCallback.hasFailed(call, t, null);
            }
        });
    }
}
