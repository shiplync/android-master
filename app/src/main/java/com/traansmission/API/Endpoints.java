package com.traansmission.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.traansmission.models.PaginatedShipments;
import com.traansmission.models.User;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by SAMBUCA on 3/7/16.
 */
public interface Endpoints {

    @GET("check_android_version/{version}/")
    Call<JsonElement> getCheckVersion(@Path("version") int version);

    @GET("users/self/")
    Call<User> getUserSelf();

    @PATCH("users/self/")
    Call<User> patchUserSelf(@Body User body);

    @POST("login/")
    Call<JsonObject> login(@Body Map<String, String> body);

    @GET("users/verify_token/")
    Call<JsonObject> getVerifyToken();

    @Multipart
    @POST("files/?path=profile_photo")
    Call<JsonElement> postUploadProfilePhoto(@Part("file\"; filename=\"picture.jpg\" ") RequestBody file);

    @GET("shipments/")
    Call<PaginatedShipments> getPaginatedShipments(@Query("page") int page);

    @GET("shipments/")
    Call<PaginatedShipments> getShipmentById(@Query("id") int id);

    @POST("geolocations/")
    Call<JsonObject> postGeolocation(@Body JsonObject body);

    @POST("platforms/")
    Call<JsonObject> postPlatform(@Body JsonObject body);

    @GET("platforms/")
    Call<JsonArray> getPlatformIdByToken(@Query("identifier") String token);

    @DELETE("platforms/{id}/")
    Call<JsonObject> deleteTokenByPlatformId(@Path("id") int id);

}
