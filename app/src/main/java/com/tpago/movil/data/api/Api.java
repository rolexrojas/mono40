package com.tpago.movil.data.api;

import com.tpago.movil.domain.user.User;

import io.reactivex.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;

/**
 * @author hecvasro
 */
public interface Api {

  @POST("signin")
  Single<Response<User>> signIn(@Body ApiSignInBody body);

  @POST("account/fingerprint-authorization")
  Single<Void> enableAltAuth(@Body ApiEnableAltAuthBody body);

  @DELETE("account/public-key/account/1")
  Single<Void> disableAltAuth();
}
