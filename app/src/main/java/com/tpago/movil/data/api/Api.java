package com.tpago.movil.data.api;

import com.tpago.movil.domain.user.User;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * @author hecvasro
 */
interface Api {

  @POST("signin")
  Single<User> signIn(@Body SignInBody body);
}
