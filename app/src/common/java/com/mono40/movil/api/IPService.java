package com.mono40.movil.api;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface IPService {
    @GET("/?format=json")
    Call<ApiResponse> getIp();

    @POST("/post")
    Call<ResponsePostResponse> echoPostman(@Body PostRequestData content);
}
