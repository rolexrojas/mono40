package com.tpago.movil.api;

import android.net.Uri;
import android.support.annotation.IntDef;

import com.tpago.movil.Code;
import com.tpago.movil.Email;
import com.tpago.movil.Name;
import com.tpago.movil.company.bank.Bank;
import com.tpago.movil.d.data.api.ChangePinResponseBody;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.insurance.micro.MicroInsurancePartner;
import com.tpago.movil.insurance.micro.MicroInsurancePlan;
import com.tpago.movil.lib.Password;
import com.tpago.movil.PhoneNumber;
import com.tpago.movil.company.partner.Partner;
import com.tpago.movil.paypal.PayPalAccount;
import com.tpago.movil.paypal.PayPalTransactionData;
import com.tpago.movil.product.disbursable.DisbursableProduct;
import com.tpago.movil.session.SessionData;
import com.tpago.movil.session.UnlockMethodSignatureData;
import com.tpago.movil.session.User;
import com.tpago.movil.transaction.TransactionSummary;
import com.tpago.movil.util.Result;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.math.BigDecimal;
import java.security.PublicKey;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * @author hecvasro
 */
public interface Api {

  // [SECTION] Auth
  Single<Result<User>> createSession(
    PhoneNumber phoneNumber,
    Email email,
    String firstName,
    String lastName,
    Password password,
    Code pin,
    String deviceId
  );

  // [SECTION] Auth
  Single<Result<RetrofitApiEmailRequestVerificationCodeResponse>> preRegisterInformation(
      String phoneNumber,
      String email,
      String firstName,
      String lastName,
      Bank bank
  );

  Single<Result<User>> createSession(
    PhoneNumber phoneNumber,
    Email email,
    Password password,
    String deviceId,
    boolean shouldDeactivatePreviousDevice
  );

  Single<Result<User>> openSession(UnlockMethodSignatureData signatureData, byte[] signedData);

  // [SECTION] Session
  Single<SessionData> fetchSessionData();

  // [SECTION] Session/Recipients

  // [session/transaction/insurance/micro]
  Single<List<MicroInsurancePlan>> fetchMicroInsurancePlans(MicroInsurancePartner partner);

  Single<Result<MicroInsurancePlan.Request>> generateMicroInsurancePurchaseRequest(
    MicroInsurancePartner partner,
    MicroInsurancePlan plan,
    MicroInsurancePlan.Term term
  );

  Single<Result<TransactionSummary>> confirmMicroInsurancePurchase(
    MicroInsurancePartner partner,
    MicroInsurancePlan plan,
    MicroInsurancePlan.Term term,
    MicroInsurancePlan.Request request,
    Product paymentMethod,
    Code pin
  );

  // [session/transaction/pay-pal]
  Single<PayPalTransactionData> fetchPayPalTransactionData(
    PayPalAccount recipient,
    Product paymentMethod,
    BigDecimal amount
  );

  Single<Result<TransactionSummary>> confirmPayPalTransaction(
    PayPalAccount recipient,
    Product paymentMethod,
    BigDecimal amount,
    PayPalTransactionData transactionData,
    Code pin
  );

  // [SECTION] Session/Transactions/Disbursement
  Single<Result<DisbursableProduct.TermData>> fetchDisbursementTermData(
    DisbursableProduct product,
    BigDecimal amount
  );

  Single<Result<DisbursableProduct.FeeData>> fetchDisbursementFeeData(
    DisbursableProduct product,
    int term
  );

  Single<Result<TransactionSummary>> confirmDisbursement(
    DisbursableProduct product,
    Code pin
  );

  Single<List<Bank>> fetchBanks();

  Single<List<DisbursableProduct>> fetchDisbursableProducts();


    // [SECTION] Session/User
  Completable updateUserName(User user, Name name);

  Single<Uri> updateUserPicture(User user, File picture);

  Completable updateUserCarrier(User user, Partner carrier);

  // [SECTION] Session/Settings

  // [SECTION] Session/Settings/UnlockMethod
  Completable enableUnlockMethod(PublicKey key);

  Completable disableUnlockMethod();

  Completable requestForgotPassword(String email);

  // [SECTION] Utils
  Single<Integer> fetchPhoneNumberState(PhoneNumber phoneNumber);

  @IntDef({
    FailureCode.ALREADY_ASSOCIATED_DEVICE,
    FailureCode.ALREADY_ASSOCIATED_PROFILE,
    FailureCode.ALREADY_REGISTERED_EMAIL,
    FailureCode.ALREADY_REGISTERED_USERNAME,
    FailureCode.INCORRECT_USERNAME_AND_PASSWORD,
    FailureCode.INVALID_DEVICE_ID,
    FailureCode.INVALID_EMAIL,
    FailureCode.INVALID_PASSWORD,
    FailureCode.INVALID_PHONE_NUMBER,
    FailureCode.INVALID_PIN,
    FailureCode.UNASSOCIATED_PHONE_NUMBER,
    FailureCode.UNASSOCIATED_PROFILE,
    FailureCode.UNAVAILABLE_SERVICE,
    FailureCode.INCORRECT_CODE
  })
  @Retention(RetentionPolicy.SOURCE)
  @interface FailureCode {

    int ALREADY_ASSOCIATED_DEVICE = 112;
    int ALREADY_ASSOCIATED_PROFILE = 12;
    int ALREADY_REGISTERED_EMAIL = 55;
    int ALREADY_REGISTERED_USERNAME = 21;
    int INCORRECT_USERNAME_AND_PASSWORD = 4;
    int INVALID_DEVICE_ID = 9;
    int INVALID_EMAIL = 0;
    int INVALID_PASSWORD = 1618;
    int INVALID_PHONE_NUMBER = 13;
    int INVALID_PIN = 16;
    int UNASSOCIATED_PHONE_NUMBER = 216;
    int UNASSOCIATED_PROFILE = 144;
    int UNAVAILABLE_SERVICE = 14;

    int INCORRECT_CODE = 7000;

    int UNAUTHORIZED = 10401;
    int UNEXPECTED = 10500;
  }

  Single<Result<Void>> ResetPasswordWithPIN(
          String password,
          String msisdn,
          String email,
          String pin
  );

  Single<Result<Void>> ChangePassword(
      String password
  );

  Single<Result<ChangePinResponseBody>> ChangePin(
      String msisdn,
      String newPin,
      String oldPin
  );

  // [SECTION] OTP
  Single<Result<Void>> requestOneTimePasswordActivationCode(String msisdn);

  Single<Result<RetrofitApiEmailRequestVerificationCodeResponse>> requestEmailOneTimePasswordActivationCode(String msisdn, String email);

  Single<Result<Void>> verifyOneTimePasswordActivationCode(String msisdn, String activationCode);

  Single<Result<Void>> verifyEmailOneTimePasswordActivationCode(String msisdn, String email, String activationCode);

}
