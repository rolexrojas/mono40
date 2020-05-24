package com.mono40.movil.api;

import android.content.Context;
import android.net.Uri;
import android.os.Build;

import com.mono40.movil.Code;
import com.mono40.movil.Email;
import com.mono40.movil.Name;
import com.mono40.movil.PhoneNumber;
import com.mono40.movil.company.bank.Bank;
import com.mono40.movil.company.partner.Partner;
import com.mono40.movil.d.data.api.ChangePasswordBody;
import com.mono40.movil.d.data.api.ChangePinBody;
import com.mono40.movil.d.data.api.ChangePinResponseBody;
import com.mono40.movil.d.data.api.ResetPasswordPINBody;
import com.mono40.movil.d.domain.Customer;
import com.mono40.movil.d.domain.Product;
import com.mono40.movil.data.api.DeviceInformationBody;
import com.mono40.movil.dep.MimeType;
import com.mono40.movil.insurance.micro.MicroInsurancePartner;
import com.mono40.movil.insurance.micro.MicroInsurancePlan;
import com.mono40.movil.lib.Password;
import com.mono40.movil.paypal.PayPalAccount;
import com.mono40.movil.paypal.PayPalTransactionData;
import com.mono40.movil.product.disbursable.DisbursableProduct;
import com.mono40.movil.session.SessionData;
import com.mono40.movil.session.UnlockMethodSignatureData;
import com.mono40.movil.session.User;
import com.mono40.movil.transaction.TransactionSummary;
import com.mono40.movil.util.ObjectHelper;
import com.mono40.movil.util.Result;

import java.io.File;
import java.math.BigDecimal;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

public final class ApiRetrofitImpl implements Api {
    Context context;

    public static ApiRetrofitImpl create(ApiRetrofit api, MapperResult.Creator resultCreator, EmptyMapperResult.Creator emptyResultCreator,
                                         Context context) {
        return new ApiRetrofitImpl(api, resultCreator, emptyResultCreator, context);
    }

    private final ApiRetrofit api;
    private final MapperResult.Creator resultCreator;
    private final EmptyMapperResult.Creator emptyResultCreator;

    private ApiRetrofitImpl(ApiRetrofit api, MapperResult.Creator resultCreator, EmptyMapperResult.Creator emptyResultCreator, Context context) {
        this.api = ObjectHelper.checkNotNull(api, "api");
        this.resultCreator = ObjectHelper.checkNotNull(resultCreator, "resultCreator");
        this.emptyResultCreator = ObjectHelper.checkNotNull(emptyResultCreator, "emptyResultCreator");
        this.context = context;
    }

    // [SECTION] Auth
    @Override
    public Single<Result<User>> createSession(
            PhoneNumber phoneNumber,
            Email email,
            String firstName,
            String lastName,
            Password password,
            Code pin,
            String deviceId
    ) {
        final RetrofitApiSignUpBody body = RetrofitApiSignUpBody.builder()
                .phoneNumber(phoneNumber)
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .password(password)
                .pin(pin)
                .deviceInformationBody(DeviceInformationBody.builder()
                        .deviceId(deviceId)
                        .manufacturer(Build.MANUFACTURER)
                        .serial(Build.SERIAL)
                        .fingerprint(Build.FINGERPRINT)
                        .device(Build.DEVICE)
                        .display(Build.DISPLAY)
                        .board(Build.BOARD)
                        .bootloader(Build.BOOTLOADER)
                        .brand(Build.BRAND)
                        .hardware(Build.HARDWARE)
                        .model(Build.MODEL)
                        .build())
                .build();
        return this.api.signUp(body)
                .map(this.resultCreator.create());
    }


    // [SECTION] Auth
    @Override
    public Single<Result<RetrofitApiEmailRequestVerificationCodeResponse>> preRegisterInformation(
            String phoneNumber,
            String email,
            String firstName,
            String lastName,
            Bank bank
    ) {
        final RetrofitApiPreRegistrationBody body = RetrofitApiPreRegistrationBody.builder()
                .phoneNumber(phoneNumber)
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .bank(bank)
                .build();
        return this.api.preRegisterInformation(body)
                .map(this.resultCreator.create());
    }

    @Override
    public Single<Result<User>> createSession(
            PhoneNumber phoneNumber,
            Email email,
            Password password,
            String deviceId,
            boolean shouldDeactivatePreviousDevice
    ) {
        final Single<Response<User>> single;
        if (shouldDeactivatePreviousDevice) {
            final RetrofitApiAssociateDeviceBody body = RetrofitApiAssociateDeviceBody.builder()
                    .phoneNumber(phoneNumber)
                    .email(email)
                    .password(password)
                    .deviceId(deviceId)
                    .deviceInformationBody(
                            DeviceInformationBody.builder()
                                    .deviceId(deviceId)
                                    .manufacturer(Build.MANUFACTURER)
                                    .serial(Build.SERIAL)
                                    .fingerprint(Build.FINGERPRINT)
                                    .device(Build.DEVICE)
                                    .display(Build.DISPLAY)
                                    .board(Build.BOARD)
                                    .bootloader(Build.BOOTLOADER)
                                    .brand(Build.BRAND)
                                    .hardware(Build.HARDWARE)
                                    .model(Build.MODEL).build()
                    )
                    .build();
            single = this.api.associateDevice(body);
        } else {
            final RetrofitApiSignInBody body = RetrofitApiSignInBody.builder()
                    .phoneNumber(phoneNumber)
                    .email(email)
                    .password(password)
                    .deviceInformationBody(DeviceInformationBody.builder()
                            .deviceId(deviceId)
                            .manufacturer(Build.MANUFACTURER)
                            .serial(Build.SERIAL)
                            .fingerprint(Build.FINGERPRINT)
                            .device(Build.DEVICE)
                            .display(Build.DISPLAY)
                            .board(Build.BOARD)
                            .bootloader(Build.BOOTLOADER)
                            .brand(Build.BRAND)
                            .hardware(Build.HARDWARE)
                            .model(Build.MODEL).build())
                    .build();
            single = this.api.signIn(body);
        }
        return single.map(this.resultCreator.create());
    }

    @Override
    public Single<Result<User>> openSession(
            UnlockMethodSignatureData signatureData,
            byte[] signedData
    ) {
        final RetrofitApiOpenSessionBody body = RetrofitApiOpenSessionBody.builder()
                .user(signatureData.user())
                .signedData(signedData)
                .deviceId(signatureData.deviceId())
                .build();
        return this.api.openSession(body)
                .map(this.resultCreator.create());
    }

    // [SECTION] Session
    private Single<List<ApiBankTransactions>> fetchBanksTransactions() {
        return this.api.fetchBanksTransactions()
                .flatMapObservable(Observable::fromIterable)
                //.filter(ApiBankTransactions::hasItems)
                .toList();
    }

    private Map<Bank, List<ApiBankTransaction>> zipBanksTransactionsIntoMap(
            List<Bank> banks,
            List<ApiBankTransactions> transactions
    ) {
        ObjectHelper.checkNotNull(banks, "banks");
        ObjectHelper.checkNotNull(transactions, "transactions");
        final Map<Bank, List<ApiBankTransaction>> map = new HashMap<>();
        for (ApiBankTransactions transaction : transactions) {
            for (Bank bank : banks) {
                if (bank.code() == transaction.code()) {
                    if (transaction.hasItems()) {
                        map.put(bank, transaction.items());
                    }
                    break;
                }
            }
        }
        return map;
    }

    private Observable<DisbursableProduct> flatMapToDisbursableProduct(
            Map.Entry<Bank, List<ApiBankTransaction>> entry
    ) {
        final Bank bank = entry.getKey();
        final BodyDisbursement body = BodyDisbursement.builder()
                .bank(bank)
                .build();
        return Observable.fromIterable(entry.getValue())
                .flatMap((transaction) -> this.api.fetchDisbursementAmountData(transaction.type(), body))
                .map(this.resultCreator.create())
                .filter(Result::isSuccessful)
                .map(Result::successData)
                .map((disbursable) -> DisbursableProduct.create(bank, disbursable));
    }

    private SessionData zipIntoSessionData(
            List<Bank> banks,
            List<Partner> partners,
            List<MicroInsurancePartner> microInsurancePartners,
            List<PayPalAccount> payPalAccounts,
            ApiSessionData sessionData
    ) {
        return SessionData.builder()
                .banks(banks)
                .partners(partners)
                .products(sessionData.products())
                .microInsurancePartners(microInsurancePartners)
                .payPalAccounts(payPalAccounts)
                .build();
    }

    @Override
    public Single<SessionData> fetchSessionData() {
        final Single<List<Bank>> banks = this.api.fetchBanks()
                .map(ApiBanks::value)
                .cache();
        final Single<List<Partner>> partners = this.api.fetchPartners()
                .map(ApiPartners::value);

        final Single<List<MicroInsurancePartner>> microInsurancePartners = this.api.fetchMicroInsurancePartners();

        final Single<List<PayPalAccount>> payPalAccounts = this.api.fetchPayPalAccounts()
                .map(ApiPayPalAccounts::value);

        return Single.zip(
                banks,
                partners,
                microInsurancePartners,
                payPalAccounts,
                this.api.fetchSessionData(),
                this::zipIntoSessionData
        );
    }

    @Override
    public Single<List<Bank>> fetchBanks() {
        final Single<List<Bank>> banks = this.api.fetchBanks()
                .map(ApiBanks::value)
                .cache();
        return banks;
    }

    @Override
    public Single<List<DisbursableProduct>> fetchDisbursableProducts() {
        final Single<List<Bank>> banks = this.api.fetchBanks()
                .map(ApiBanks::value)
                .cache();
        final Single<List<DisbursableProduct>> disbursableProducts = banks
                .zipWith(this.fetchBanksTransactions(), this::zipBanksTransactionsIntoMap)
                .map(Map::entrySet)
                .flatMapObservable(Observable::fromIterable)
                .flatMap(this::flatMapToDisbursableProduct)
                .toList();

        return disbursableProducts;
    }

    @Override
    public Single<List<MicroInsurancePlan>> fetchMicroInsurancePlans(
            MicroInsurancePartner partner
    ) {
        final ApiMicroInsuranceBody body = ApiMicroInsuranceBody.builder()
                .partner(partner)
                .build();
        return this.api.fetchMicroInsurancePlans(body)
                .map(ApiMicroInsurancePlans::plans);
    }

    @Override
    public Single<Result<MicroInsurancePlan.Request>> generateMicroInsurancePurchaseRequest(
            MicroInsurancePartner partner,
            MicroInsurancePlan plan,
            MicroInsurancePlan.Term term
    ) {
        final ApiMicroInsuranceBody body = ApiMicroInsuranceBody.builder()
                .partner(partner)
                .plan(plan)
                .term(term)
                .build();
        return this.api.generateMicroInsurancePurchaseRequest(body)
                .map(this.resultCreator.create());
    }

    @Override
    public Single<Result<TransactionSummary>> confirmMicroInsurancePurchase(
            MicroInsurancePartner partner,
            MicroInsurancePlan plan,
            MicroInsurancePlan.Term term,
            MicroInsurancePlan.Request request,
            Product paymentMethod,
            Code pin
    ) {
        final ApiMicroInsuranceBody body = ApiMicroInsuranceBody.builder()
                .partner(partner)
                .plan(plan)
                .term(term)
                .request(request)
                .paymentMethod(paymentMethod)
                .pin(pin)
                .build();
        return this.api.confirmMicroInsurancePurchase(body)
                .map(this.resultCreator.create());
    }

    @Override
    public Single<PayPalTransactionData> fetchPayPalTransactionData(
            PayPalAccount recipient,
            Product paymentMethod,
            BigDecimal amount
    ) {
        final ApiPayPalTransactionBody body = ApiPayPalTransactionBody.builder()
                .recipient(recipient)
                .paymentMethod(paymentMethod)
                .amount(amount)
                .build();
        return this.api.fetchPayPalTransactionData(body);
    }

    @Override
    public Single<Result<TransactionSummary>> confirmPayPalTransaction(
            PayPalAccount recipient,
            Product paymentMethod,
            BigDecimal amount,
            PayPalTransactionData transactionData,
            Code pin
    ) {
        final ApiPayPalTransactionBody body = ApiPayPalTransactionBody.builder()
                .recipient(recipient)
                .paymentMethod(paymentMethod)
                .amount(amount)
                .payPalFee(transactionData.payPalFee())
                .gcsFee(transactionData.gcsFee())
                .bankFee(transactionData.bankFee())
                .customerFee(transactionData.customerFee())
                .rate(transactionData.rate())
                .tax(transactionData.tax())
                .total(transactionData.total())
                .pin(pin.value())
                .build();
        return this.api.confirmPayPalTransaction(body)
                .map(this.resultCreator.create());
    }

    // [SECTION] Session/Recipients

    // [SECTION] Session/Transactions

    // [SECTION] Session/Transactions/Disbursement
    @Override
    public Single<Result<DisbursableProduct.TermData>> fetchDisbursementTermData(
            DisbursableProduct product,
            BigDecimal amount
    ) {
        final BodyDisbursement body = BodyDisbursement.builder(product)
                .amount(amount)
                .build();
        return this.api.fetchDisbursementTermData(product.type(), body)
                .map(this.resultCreator.create());
    }

    @Override
    public Single<Result<DisbursableProduct.FeeData>> fetchDisbursementFeeData(
            DisbursableProduct product,
            int term
    ) {
        final BodyDisbursement body = BodyDisbursement.builder(product)
                .term(term)
                .build();
        return this.api.fetchDisbursementFeeData(product.type(), body)
                .map(this.resultCreator.create());
    }

    @Override
    public Single<Result<TransactionSummary>> confirmDisbursement(
            DisbursableProduct product,
            Code pin
    ) {
        final BodyDisbursement body = BodyDisbursement.builder(product)
                .pin(pin.value())
                .build();
        return this.api.confirmDisbursement(product.type(), body)
                .map(this.resultCreator.create());
    }


    // [SECTION] Session/User
    @Override
    public Completable updateUserName(User user, Name name) {
        ObjectHelper.checkNotNull(user, "user");
        ObjectHelper.checkNotNull(name, "name");
        return this.api.updateUserName(ApiName.create(name));
    }

    @Override
    public Single<Uri> updateUserPicture(User user, File picture) {
        ObjectHelper.checkNotNull(user, "user");
        ObjectHelper.checkNotNull(picture, "picture");
        final RequestBody requestPicture = RequestBody
                .create(MediaType.parse(MimeType.IMAGE), picture);
        final MultipartBody.Part body = MultipartBody.Part
                .createFormData("file", picture.getName(), requestPicture);
        return this.api.updateUserPicture(body)
                .map(User::picture);
    }

    @Override
    public Completable updateUserCarrier(User user, Partner carrier) {
        ObjectHelper.checkNotNull(user, "user");
        ObjectHelper.checkNotNull(carrier, "carrier");
        return this.api.updateBeneficiary(ApiBeneficiary.create(user, carrier));
    }

    // [SECTION] Session/Settings

    // [SECTION] Session/Settings/UnlockMethod
    @Override
    public Completable enableUnlockMethod(PublicKey publicKey) {
        final RetrofitApiEnableSessionOpeningBody body = RetrofitApiEnableSessionOpeningBody.builder()
                .publicKey(publicKey)
                .build();
        return this.api.enableSessionOpeningMethod(body);
    }

    @Override
    public Completable disableUnlockMethod() {
        return this.api.disableSessionOpeningMethod();
    }

    @Override
    public Completable requestForgotPassword(String email) {
        return this.api.requestForgotPassword(email);
    }

    // [SECTION] Utils
    @Override
    public Single<Integer> fetchPhoneNumberState(PhoneNumber phoneNumber) {
        return this.api.fetchPhoneNumberState(phoneNumber.value())
                .map(ApiPhoneNumberState::state);
    }

    @Override
    public Single<Result<Void>> ResetPasswordWithPIN(
            String password,
            String msisdn,
            String email,
            String pin
    ) {
        ResetPasswordPINBody body = ResetPasswordPINBody.create(password, msisdn, email, pin);
        return this.api.resetPasswordWithPIN(body).map(this.emptyResultCreator.create());
    }

    @Override
    public Single<Result<Void>> ChangePassword(
            String password
    ) {
        ChangePasswordBody body = ChangePasswordBody.create(password);
        return this.api.changePassword(body).map(this.emptyResultCreator.create());
    }

    @Override
    public Single<Result<ChangePinResponseBody>> ChangePin(String msisdn, String newPin, String oldPin) {
        ChangePinBody body = ChangePinBody.create(msisdn, newPin, oldPin);
        return this.api.changePin(body).map(this.resultCreator.create());
    }

    public Single<Result<Void>> requestOneTimePasswordActivationCode(String msisdn) {
        return this.api.requestOneTimePasswordActivationCode(msisdn)
                .map(this.emptyResultCreator.create());
    }

    public Single<Result<RetrofitApiEmailRequestVerificationCodeResponse>> requestEmailOneTimePasswordActivationCode(String msisdn, String email) {
        return this.api.requestEmailOneTimePasswordActivationCode(msisdn, email)
                .map(this.resultCreator.create());
    }


    @Override
    public Single<Result<Void>> verifyOneTimePasswordActivationCode(String msisdn, String activationCode) {
        VerifyActivationCodeBody body = VerifyActivationCodeBody.create(msisdn, activationCode);
        return this.api.verifyOneTimePasswordActivationCode(body)
                .map(this.emptyResultCreator.create());
    }

    @Override
    public Single<Result<Void>> verifyEmailOneTimePasswordActivationCode(String msisdn, String email, String code) {
        EmailVerifcationCodeBody body = EmailVerifcationCodeBody.create(msisdn, email, code);
        return this.api.verifyEmailOneTimePasswordActivationCode(body)
                .map(this.emptyResultCreator.create());
    }

    @Override
    public Single<Result<Customer>> fetchCustomer(String phoneNumber) {
        return this.api.fetchCustomer(phoneNumber)
                .map(resultCreator.create());
    }

    @Override
    public Single<Result<ApiSecretTokenResponse>> getQrForCustomer() {
        return this.api.getQrForGustomer()
                .map(resultCreator.create());
    }
}
