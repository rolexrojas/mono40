package com.tpago.movil.data.api;

import com.tpago.movil.api.ApiBanks;
import com.tpago.movil.api.ApiBeneficiary;
import com.tpago.movil.api.ApiName;
import com.tpago.movil.api.ApiPhoneNumberState;
import com.tpago.movil.api.RetrofitApiAssociateDeviceBody;
import com.tpago.movil.api.RetrofitApiEnableSessionOpeningBody;
import com.tpago.movil.api.RetrofitApiOpenSessionBody;
import com.tpago.movil.api.RetrofitApiSignInBody;
import com.tpago.movil.api.RetrofitApiSignUpBody;
import com.tpago.movil.session.User;

import io.reactivex.Completable;
import io.reactivex.Single;
import okhttp3.MultipartBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * @author hecvasro
 */
public interface RetrofitApi {

  @GET("banks")
  Single<ApiBanks> fetchBanks();

 Single<ApiPhoneNumberState> fetchPhoneNumberState(@Path("phoneNumber") String phoneNumber);

  @POST("signup")
  Single<Response<User>> signUp(@Body RetrofitApiSignUpBody body);

  @POST("associate")
  Single<Response<User>> associateDevice(@Body RetrofitApiAssociateDeviceBody body);

  @POST("signin")
  Single<Response<User>> signIn(@Body RetrofitApiSignInBody body);

  @POST("account/fingerprint-authorization")
  Completable enableSessionOpeningMethod(@Body RetrofitApiEnableSessionOpeningBody body);

  @POST("signin/fingerprint")
  Single<Response<User>> openSession(@Body RetrofitApiOpenSessionBody body);

  @DELETE("account/public-key/account")
  Completable disableSessionOpeningMethod();

  @POST("account/forgot-password?email={email}")
  Completable requestForgotPassword();

  @PUT("account")
  Completable updateUserName(@Body ApiName body);

  @Multipart
  @POST("account/upload/profile-pic")
  Single<User> updateUserPicture(@Part MultipartBody.Part body);

  @PUT("beneficiary")
  Completable updateBeneficiary(@Body ApiBeneficiary body);
}
