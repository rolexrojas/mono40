package com.tpago.movil.dep.api;

import com.tpago.movil.data.api.PhoneNumberState;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * @author hecvasro
 */
@Deprecated
interface DApiService {

  @GET("customer/{phoneNumber}/status")
  Single<Response<PhoneNumberState>> validatePhoneNumber(
    @Path("phoneNumber") String phoneNumber
  );

  @POST("signup")
  Single<Response<UserData>> signUp(@Body SignUpRequestBody body);

  @POST("signin")
  Single<Response<UserData>> signIn(@Body SignInRequestBody body);

  @POST("associate")
  Single<Response<UserData>> associate(@Body AssociateRequestBody body);
}
