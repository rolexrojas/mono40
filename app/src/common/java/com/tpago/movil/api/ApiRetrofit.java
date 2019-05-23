package com.tpago.movil.api;

import com.tpago.movil.d.data.api.ChangePasswordBody;
import com.tpago.movil.d.data.api.ChangePinBody;
import com.tpago.movil.d.data.api.ChangePinResponseBody;
import com.tpago.movil.d.data.api.ResetPasswordPINBody;
import com.tpago.movil.d.domain.Customer;
import com.tpago.movil.insurance.micro.MicroInsurancePartner;
import com.tpago.movil.insurance.micro.MicroInsurancePlan;
import com.tpago.movil.paypal.PayPalTransactionData;
import com.tpago.movil.product.disbursable.Disbursable;
import com.tpago.movil.product.disbursable.DisbursableProduct;
import com.tpago.movil.session.User;
import com.tpago.movil.transaction.TransactionSummary;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.MultipartBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiRetrofit {

    // [SECTION] Auth
    @POST("signup")
    Single<Response<User>> signUp(@Body RetrofitApiSignUpBody body,
                                  @Header("version") String version,
                                  @Header("os") String os);

    @POST("customer/pre-registration-information")
    Single<Response<RetrofitApiEmailRequestVerificationCodeResponse>> preRegisterInformation(@Body RetrofitApiPreRegistrationBody body);

    @POST("associate")
    Single<Response<User>> associateDevice(@Body RetrofitApiAssociateDeviceBody body,
                                           @Header("version") String version,
                                           @Header("os") String os);

    @POST("signin")
    Single<Response<User>> signIn(@Body RetrofitApiSignInBody body,
                                  @Header("version") String version,
                                  @Header("os") String os);

    @POST("signin/fingerprint")
    Single<Response<User>> openSession(@Body RetrofitApiOpenSessionBody body,
                                       @Header("version") String version,
                                       @Header("os") String os);

    // [SECTION] Session
    @GET("banks")
    Single<ApiBanks> fetchBanks();

    @GET("payments/partners")
    Single<ApiPartners> fetchPartners();

    @GET("initial-load")
    Single<ApiSessionData> fetchSessionData();

    // [SECTION] Session/Recipients
    @PUT("beneficiary")
    Completable updateBeneficiary(@Body ApiBeneficiary body);

    // [SECTION] Session/Transactions

    // [session/transaction/insurance/micro]
    @GET("micro-insurance/partners")
    Single<List<MicroInsurancePartner>> fetchMicroInsurancePartners();

    @POST("micro-insurance/plans")
    Single<ApiMicroInsurancePlans> fetchMicroInsurancePlans(@Body ApiMicroInsuranceBody body);

    @POST("micro-insurance/generate-request")
    Single<Response<MicroInsurancePlan.Request>> generateMicroInsurancePurchaseRequest(@Body ApiMicroInsuranceBody body);

    @POST("micro-insurance/issue-request")
    Single<Response<TransactionSummary>> confirmMicroInsurancePurchase(@Body ApiMicroInsuranceBody body);

    // [session/transaction/pay-pal]
    @GET("paypal/accounts")
    Single<ApiPayPalAccounts> fetchPayPalAccounts();

    @POST("paypal/accounts/calculate-fee")
    Single<PayPalTransactionData> fetchPayPalTransactionData(@Body ApiPayPalTransactionBody body);

    @POST("paypal/accounts/recharge")
    Single<Response<TransactionSummary>> confirmPayPalTransaction(@Body ApiPayPalTransactionBody body);

    // [SECTION] Session/Transactions/Disbursement
    @GET("disbursement/banks")
    Single<List<ApiBankTransactions>> fetchBanksTransactions();

    @POST("disbursement/available-amount/{type}")
    Observable<Response<Disbursable>> fetchDisbursementAmountData(
            @Path("type") String type,
            @Body BodyDisbursement body
    );

    @POST("disbursement/amount-fees/{type}")
    Single<Response<DisbursableProduct.TermData>> fetchDisbursementTermData(
            @Path("type") String type,
            @Body BodyDisbursement body
    );

    @POST("disbursement/calculate-fee/{type}")
    Single<Response<DisbursableProduct.FeeData>> fetchDisbursementFeeData(
            @Path("type") String type,
            @Body BodyDisbursement body
    );

    @POST("disbursement/confirm/{type}")
    Single<Response<TransactionSummary>> confirmDisbursement(
            @Path("type") String type,
            @Body BodyDisbursement body
    );

    // [SECTION] Session/User
    @PUT("account")
    Completable updateUserName(@Body ApiName body);

    @Multipart
    @POST("account/upload/profile-pic")
    Single<User> updateUserPicture(@Part MultipartBody.Part body);

    // [SECTION] Session/Settings

    // [SECTION] Session/Settings/Unlock
    @POST("account/fingerprint-authorization")
    Completable enableSessionOpeningMethod(@Body RetrofitApiEnableSessionOpeningBody body);

    @DELETE("account/public-key/account")
    Completable disableSessionOpeningMethod();

    @POST("account/forgot-password")
    Completable requestForgotPassword(@Query("email") String email);

    // [SECTION] Utils
    @GET("customer/{phoneNumber}/status")
    Single<ApiPhoneNumberState> fetchPhoneNumberState(@Path("phoneNumber") String phoneNumber,
                                                      @Header("version") String version,
                                                      @Header("os") String os,
                                                      @Query("manufacturer") String manufacturer,
                                                      @Query("serial") String serial,
                                                      @Query("fingerprint") String fingerprint,
                                                      @Query("device") String device,
                                                      @Query("display") String display,
                                                      @Query("board") String board,
                                                      @Query("bootloader") String bootloader,
                                                      @Query("brand") String brand,
                                                      @Query("hardware") String hardware,
                                                      @Query("model") String model);

    @POST("account/reset-password-pin")
    Single<Response<Void>> resetPasswordWithPIN(@Body ResetPasswordPINBody body);

    @POST("account/change-password")
    Single<Response<Void>> changePassword(@Body ChangePasswordBody body);

    @POST("change/pin")
    Single<Response<ChangePinResponseBody>> changePin(@Body ChangePinBody body);

    //[SECTION] OTP
    @GET("signup/activation-code")
    Single<Response<Void>> requestOneTimePasswordActivationCode(@Query("msisdn") String msisdn);

    @GET("validate/email/send-verification-code")
    Single<Response<RetrofitApiEmailRequestVerificationCodeResponse>> requestEmailOneTimePasswordActivationCode(@Query("msisdn") String msisdn, @Query("email") String email);

    @POST("signup/verify-activation-code")
    Single<Response<Void>> verifyOneTimePasswordActivationCode(@Body VerifyActivationCodeBody body);

    @POST("validate/email/check-verification-code")
    Single<Response<Void>> verifyEmailOneTimePasswordActivationCode(@Body EmailVerifcationCodeBody body);

    @GET("transfer/recipient-info")
    Single<Response<Customer>> fetchCustomer(@Query("recipient-msisdn") String phoneNumber);
}
