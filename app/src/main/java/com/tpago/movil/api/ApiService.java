package com.tpago.movil.api;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * @author hecvasro
 */
interface ApiService {
  @GET("customer/{phoneNumber}/status")
  Single<Response<ValidatePhoneNumberResponseData>> validatePhoneNumber(
    @Path("phoneNumber") String phoneNumber);

  @POST("signup")
  Single<Response<AuthResponseData>> signUp(@Body SignUpBody body);
}
