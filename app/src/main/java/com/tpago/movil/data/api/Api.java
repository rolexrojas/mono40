package com.tpago.movil.data.api;

import com.tpago.movil.domain.user.User;
import com.tpago.movil.util.Placeholder;

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
  Single<Placeholder> enableAltAuth(@Body ApiEnableAltAuthBody body);

  @POST("signin/fingerprint")
  Single<Response<User>> verifySignedData(@Body ApiVerifyAltAuthBody body);

  @DELETE("account/public-key/account/1")
  Single<Placeholder> disableAltAuth();
}
