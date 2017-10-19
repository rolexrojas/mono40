package com.tpago.movil.data.api;

import com.tpago.movil.session.User;
import com.tpago.movil.util.Placeholder;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;

/**
 * @author hecvasro
 */
public interface RetrofitApi {

  @POST("signup")
  Single<Response<User>> signUp(@Body RetrofitApiSignUpBody body);

  @POST("signin")
  Single<Response<User>> associateDevice(@Body RetrofitApiAssociateDeviceBody body);

  @POST("signin")
  Single<Response<User>> signIn(@Body RetrofitApiSignInBody body);

  @POST("account/fingerprint-authorization")
  Completable enableAltAuthMethod(@Body RetrofitApiEnableAltAuthBody body);

  @POST("signin/fingerprint")
  Single<Response<Placeholder>> verifyAltAuthMethod(@Body RetrofitApiVerifyAltAuthBody body);

  @DELETE("account/public-key/account")
  Completable disableAltAuthMethod();
}
