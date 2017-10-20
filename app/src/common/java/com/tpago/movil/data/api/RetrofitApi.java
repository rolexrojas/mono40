package com.tpago.movil.data.api;

import com.tpago.movil.session.User;

import io.reactivex.Completable;
import io.reactivex.Single;
import okhttp3.MultipartBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

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
  Completable enableSessionOpeningMethod(@Body RetrofitApiEnableSessionOpeningBody body);

  @POST("signin/fingerprint")
  Single<Response<User>> openSession(@Body RetrofitApiOpenSessionBody body);

  @DELETE("account/public-key/account")
  Completable disableSessionOpeningMethod();

  @PUT("account")
  Completable updateUserName(@Body ApiName body);

  @Multipart
  @POST("account/upload/profile-pic")
  Single<User> updateUserPicture(@Part MultipartBody.Part body);

  @PUT("beneficiary")
  Completable updateBeneficiary(@Body ApiBeneficiary body);
}
