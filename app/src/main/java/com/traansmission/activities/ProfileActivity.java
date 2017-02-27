package com.traansmission.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.traansmission.R;
import com.traansmission.api.RestClient;
import com.traansmission.models.User;
import com.traansmission.shared.Auth;
import com.traansmission.shared.Constants;
import com.traansmission.shared.CurrentUser;
import com.traansmission.shared.ErrorHandler;
import com.traansmission.utils.FileUtils;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    Toolbar toolbar;
    private EditText mFirstNameView;
    private EditText mLastNameView;
    private EditText mEmailView;
    private EditText mPhoneView;
    private EditText mCompanyNameView;
    private Button mEditSaveButton;
    private ImageView mProfilePhoto;
    private boolean isEditing;
    private Uri mTempPhotoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        toolbar = (Toolbar) findViewById(R.id.toolbar_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        isEditing = false;

        mFirstNameView = (EditText) findViewById(R.id.profile_first_name);
        mLastNameView = (EditText) findViewById(R.id.profile_last_name);
        mEmailView = (EditText) findViewById(R.id.profile_email);
        mPhoneView = (EditText) findViewById(R.id.profile_phone);
        mPhoneView.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        mCompanyNameView = (EditText) findViewById(R.id.profile_company_name);
        mProfilePhoto = (ImageView) findViewById(R.id.profile_profile_photo);
        mEditSaveButton = (Button) findViewById(R.id.profile_edit_save_btn);

        populateViews();

        mProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEditing) {
                    Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    getIntent.setType("image/*");

                    Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickIntent.setType("image/*");

                    Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                    startActivityForResult(chooserIntent, 0);
                }
            }
        });

        mEditSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEditing) {
                    finishEditing();
                    isEditing = false;
                } else {
                    setEditing();
                    isEditing = true;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        toolbar.inflateMenu(R.menu.menu_profile);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miLogout:
                promptLogout();
        }
        return true;
    }

    private void promptLogout() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Log out")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        String token = sharedPreferences.getString("device_token",null);
                        removeTokenAndFinish(token);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void localLogout() {
        Auth.logout(getApplicationContext());
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void removeTokenAndFinish(String token) {

        //get token from GCM has failed
        if(token == null){
            localLogout();
            return;
        }
        Call<JsonArray> call = RestClient.getInstance(getApplicationContext()).apiServiceAuthenticated().getPlatformIdByToken(token);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                getPlatformIdSuccess(call, response);
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                promptLogoutFailure();
            }
        });
    }

    private void getPlatformIdSuccess(Call<JsonArray> _call, Response<JsonArray> response) {
        JsonArray ja = response.body();

        //register token to server has failed
        if(ja.size() == 0){
            localLogout();
            return;
        }

        JsonObject jo = (JsonObject) ja.get(0);
        int id = jo.get("id").getAsInt();

        Call<JsonObject> call = RestClient.getInstance(getApplicationContext()).apiServiceAuthenticated().deleteTokenByPlatformId(id);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                localLogout();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                promptLogoutFailure();
            }
        });


    }

    private void promptLogoutFailure() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Failed to log out")
                .setMessage("Failed to delete token. Please check Internet connection and retry!")
                .setNegativeButton("Okay", null)
                .show();
    }

    private void populateViews() {
        mEditSaveButton.setText("Edit");
        User user = CurrentUser.getInstance().getCurrentUser();
        populateSingleView(mFirstNameView, user.first_name);
        populateSingleView(mLastNameView, user.last_name);
        populateSingleView(mEmailView, user.email);
        populateSingleView(mPhoneView, user.phone);
        mPhoneView.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        populateSingleView(mCompanyNameView, user.company.company_name);
        if (user.profile_photo != null && user.profile_photo.file_url != null) {
            Picasso.with(this).load(user.profile_photo.file_url).into(mProfilePhoto);
        }
    }

    private void populateSingleView(EditText v, String value) {
        v.setText(value);
        v.setClickable(false);
        v.setFocusable(false);
        v.setFocusableInTouchMode(false);
        v.setTextIsSelectable(false);
    }

    private void setEditing() {
        setSingleViewEditable(mFirstNameView);
        setSingleViewEditable(mLastNameView);
        setSingleViewEditable(mEmailView);
        setSingleViewEditable(mPhoneView);
        mFirstNameView.requestFocus();
        mEditSaveButton.setText("Save");
    }

    private void setSingleViewEditable(EditText v) {
        v.setFocusable(true);
        v.setClickable(true);
        v.setFocusableInTouchMode(true);
        v.setTextIsSelectable(true);
    }

    private void finishEditing() {
        mEditSaveButton.setEnabled(false);
        User data = new User();
        data.first_name = mFirstNameView.getText().toString();
        data.last_name = mLastNameView.getText().toString();
        data.phone = mPhoneView.getText().toString();
        data.email = mEmailView.getText().toString();
        Call call = RestClient.getInstance(this).apiServiceAuthenticated().patchUserSelf(data);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.code() == 200) {
                    CurrentUser.getInstance().setCurrentUser((User) response.body());
                    populateViews();
                } else {
                    ErrorHandler.getInstance().processError(null, "Unable to save info", getApplicationContext(), new ErrorHandler.ErrorCallback() {
                        @Override
                        public void alertDismissed(Constants.ERR_TYPE errorType) {
                            populateViews();
                        }
                    });
                }
                mEditSaveButton.setEnabled(true);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                ErrorHandler.getInstance().processError(t, "", getApplicationContext(), new ErrorHandler.ErrorCallback() {
                    @Override
                    public void alertDismissed(Constants.ERR_TYPE errorType) {
                        populateViews();
                    }
                });
                mEditSaveButton.setEnabled(true);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == this.RESULT_OK) {
                mTempPhotoUri = data.getData();
                MediaType mediaType = MediaType.parse("image/jpg");
                String realPath = FileUtils.getPath(this, mTempPhotoUri);
                File file = new File(realPath);
                RequestBody requestBody = RequestBody.create(mediaType, file);
                Call<JsonElement> call = RestClient.getInstance(this).apiServiceAuthenticated().postUploadProfilePhoto(requestBody);
                call.enqueue(new Callback<JsonElement>() {
                    @Override
                    public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                    }

                    @Override
                    public void onFailure(Call<JsonElement> call, Throwable t) {
                        ErrorHandler.getInstance().processError(t, "Unable to upload image", getApplicationContext(), new ErrorHandler.ErrorCallback() {
                            @Override
                            public void alertDismissed(Constants.ERR_TYPE errorType) {

                            }
                        });
                    }
                });
            }
        }
    }

}