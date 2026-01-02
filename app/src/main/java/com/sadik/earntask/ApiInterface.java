package com.sadik.earntask;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
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

    @GET("get_tasks.php")
    Call<ResponseBody> getTasks();

    @FormUrlEncoded
    @POST("add_micro_task.php")
    Call<ResponseBody> addMicroTask(
            @Field("title") String title,
            @Field("description") String desc,
            @Field("reward") String reward,
            @Field("task_url") String url
    );

    @FormUrlEncoded
    @POST("submit_task.php")
    Call<ResponseBody> submitTask(
            @Field("user_id") String uid,
            @Field("task_id") String taskId,
            @Field("proof") String proof
    );
    @FormUrlEncoded
    @POST("deposit_request.php")
    Call<ResponseBody> deposit(
            @Field("user_id") String uid,
            @Field("amount") String amount,
            @Field("from_number") String from,
            @Field("method") String method,
            @Field("trxid") String trx
    );

    @FormUrlEncoded
    @POST("get_balance.php")
    Call<ResponseBody> getBalance(@Field("user_id") String uid);



}
