package com.sadik.earntask;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("register.php")
    Call<ResponseBody> register(
            @Field("name") String name,
            @Field("phone") String phone,
            @Field("email") String email,
            @Field("password") String password,
            @Field("referral_code") String referral
    );

    @FormUrlEncoded
    @POST("login.php")
    Call<ResponseBody> login(
            @Field("email") String email,
            @Field("password") String password
    );


    @FormUrlEncoded
    @POST("claim_referral.php")
    Call<ResponseBody> claimReferral(
            @Field("user_id") String uid,
            @Field("code") String code,
            @Field("device_hash") String device
    );


}
