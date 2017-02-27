package com.traansmission.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.crashlytics.android.Crashlytics;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.traansmission.R;
import com.traansmission.api.Endpoints;
import com.traansmission.api.RestClient;
import com.traansmission.models.User;
import com.traansmission.services.RegistrationIntentService;
import com.traansmission.shared.Constants;
import com.traansmission.shared.ErrorHandler.ErrorCallback;
import com.traansmission.shared.ErrorHandler;
import com.traansmission.shared.Initializers;
import com.traansmission.shared.Notification;


import io.fabric.sdk.android.Fabric;
import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    Endpoints apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        apiService = RestClient.getInstance(this).apiService();
        initVersionCheck();

        String token = Notification.getDeviceToken(getApplicationContext());
        if(token == null){
            Intent registrationIntent = new Intent(getApplicationContext(), RegistrationIntentService.class);
            startService(registrationIntent);
        }

    }

    private void initVersionCheck() {
        Initializers.getInstance().initVersionCheck(this, new Initializers.InitVersionResponse() {
            @Override
            public void hasFinishedWithResponse(boolean versionOK) {
                if(versionOK) {
                    initTokenCheck();
                } else {
                    gotoActivity(UpdateVersionActivity.class);
                }
            }

            @Override
            public void hasFailed(Call<JsonElement> call, Throwable throwable, Response<JsonElement> response) {
                ErrorHandler.getInstance().processError(throwable, "", getApplicationContext(), new ErrorCallback() {
                    @Override
                    public void alertDismissed(Constants.ERR_TYPE errorType) {
                        initVersionCheck();
                    }
                });
            }
        });
    }

    private void initTokenCheck() {
        Initializers.getInstance().initTokenCheck(this, new Initializers.InitTokenResponse() {
            @Override
            public void hasFinishedWithResponse(boolean tokenOK) {
                if(tokenOK) {
                    initUser();
                } else {
                    // Token not valid
                    gotoActivity(StartActivity.class);
                }
            }

            @Override
            public void hasFailed(Call<JsonObject> call, Throwable throwable, Response<JsonObject> response) {
                if(response != null && response.code()==401) {
                    // Token not valid
                    gotoActivity(StartActivity.class);
                } else {
                    ErrorHandler.getInstance().processError(throwable, "", getApplicationContext(), new ErrorCallback() {
                        @Override
                        public void alertDismissed(Constants.ERR_TYPE errorType) {
                            if (errorType==Constants.ERR_TYPE.NETWORK) {
                                initTokenCheck();
                            } else {
                                gotoActivity(StartActivity.class);
                            }
                        }
                    });
                }
            }
        });
    }

    private void initUser() {
        Initializers.getInstance().initUser(this, new Initializers.InitUserResponse() {
            @Override
            public void hasFinishedWithResponse(User user) {
                gotoActivity(ShipmentsActivity.class);
            }

            @Override
            public void hasFailed(Call<User> call, Throwable throwable, Response<User> response) {
                ErrorHandler.getInstance().processError(throwable, "", getApplicationContext(), new ErrorCallback() {
                    @Override
                    public void alertDismissed(Constants.ERR_TYPE errorType) {
                        if (errorType==Constants.ERR_TYPE.NETWORK) {
                            initTokenCheck();
                        } else {
                            gotoActivity(StartActivity.class);
                        }
                    }
                });
            }
        });
    }

    private void gotoActivity(Class activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
        MainActivity.this.finish();
    }

}
