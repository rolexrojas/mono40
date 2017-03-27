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
  Single<Response<AuthResponseBody>> signUp(@Body SignUpRequestBody body);

  @POST("signin")
  Single<Response<AuthResponseBody>> signIn(@Body SignInRequestBody body);

  @POST("associate")
  Single<Response<AuthResponseBody>> associate(@Body AssociateRequestBody body);
}
