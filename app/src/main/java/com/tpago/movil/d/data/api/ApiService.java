package com.tpago.movil.d.data.api;

import com.tpago.movil.d.domain.AccountBalance;
import com.tpago.movil.d.domain.BillBalance;
import com.tpago.movil.d.domain.CreditCardBalance;
import com.tpago.movil.d.domain.CreditCardBillBalance;
import com.tpago.movil.d.domain.Customer;
import com.tpago.movil.d.domain.InitialData;
import com.tpago.movil.d.domain.LoanBalance;
import com.tpago.movil.d.domain.LoanBillBalance;
import com.tpago.movil.d.domain.PaymentResult;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.ProductInfo;
import com.tpago.movil.d.domain.Transaction;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author hecvasro
 */
@Deprecated
interface ApiService {
  @GET("initial-load")
  Observable<Response<InitialData>> initialLoad(@Header(Api.Header.AUTHORIZATION) String authToken);

  @POST("query/accounts/balance")
  Observable<Response<AccountBalance>> accountBalance(
    @Header(Api.Header.AUTHORIZATION) String authToken,
    @Body BalanceQueryRequestBody body);

  @POST("query/credit-cards/balance")
  Observable<Response<CreditCardBalance>> creditCardBalance(
    @Header(Api.Header.AUTHORIZATION) String authToken,
    @Body BalanceQueryRequestBody body);

  @POST("query/loans/balance")
  Observable<Response<LoanBalance>> loanBalance(
    @Header(Api.Header.AUTHORIZATION) String authToken,
    @Body BalanceQueryRequestBody body);

  @GET("transaction-history")
  Observable<Response<List<Transaction>>> recentTransactions(
    @Header(Api.Header.AUTHORIZATION) String authToken);

  @GET("transfer/recipient-info")
  Observable<Response<Void>> checkIfAssociated(
    @Header(Api.Header.AUTHORIZATION) String authToken,
    @Query("recipient-msisdn") String phoneNumber);

  @GET("customer/{phoneNumber}/status")
  Observable<Response<FetchCustomerStateResponseBody>> fetchCustomerStatus(
    @Header(Api.Header.AUTHORIZATION) String authToken,
    @Path("phoneNumber") String phoneNumber);

  @GET("transfer/recipient-info")
  Observable<Response<Customer>> fetchCustomer(
    @Header(Api.Header.AUTHORIZATION) String authToken,
    @Query("recipient-msisdn") String phoneNumber);


  @POST("transfer/gcs-gcs")
  Observable<Response<TransferResponseBody>> transferToAffiliated(
    @Header(Api.Header.AUTHORIZATION) String authToken,
    @Body TransferToAffiliatedRequestBody body);

  @POST("transfer/gcs-non")
  Observable<Response<TransferResponseBody>> transferToNonAffiliated(
    @Header(Api.Header.AUTHORIZATION) String authToken,
    @Body TransferToNonAffiliatedRequestBody body);

  @POST("payments/change-default-account")
  Observable<Response<Void>> setDefaultPaymentOption(
    @Header(Api.Header.AUTHORIZATION) String authToken,
    @Body Map<String, String> body);

  @GET("banks")
  Observable<Response<BankListRequestResponse>> banks(
    @Header(Api.Header.AUTHORIZATION) String authToken);

  @POST("transfer/recipient-account-info")
  Observable<Response<ProductInfo>> fetchProductInfo(
    @Header(Api.Header.AUTHORIZATION) String authToken,
    @Body RecipientAccountInfoRequestBody body);

  @GET("payments/partners")
  Observable<Response<PartnerListRequestResponse>> partners(
    @Header(Api.Header.AUTHORIZATION) String authToken);

  @POST("payments/invoices")
  Observable<Response<Void>> addBill(
    @Header(Api.Header.AUTHORIZATION) String authToken,
    @Body BillRequestBody body);

  @POST("payments/invoices/balance")
  Observable<Response<BillBalance>> queryBalance(
    @Header(Api.Header.AUTHORIZATION) String authToken,
    @Body BillRequestBody body);

  @POST("payment/credit-cards/balance")
  Observable<Response<CreditCardBillBalance>> queryCreditCardBillBalance(
    @Header(Api.Header.AUTHORIZATION) String authToken,
    @Body Product body);

  @POST("payment/loans/balance")
  Observable<Response<LoanBillBalance>> queryLoanBalance(
    @Header(Api.Header.AUTHORIZATION) String authToken,
    @Body Product body);

  @GET("payments/invoices")
  Observable<Response<List<BillResponseBody>>> getBills(
    @Header(Api.Header.AUTHORIZATION) String authToken);

  @POST("payments/invoices/pay")
  Observable<Response<Void>> payBill(
    @Header(Api.Header.AUTHORIZATION) String authToken,
    @Body PayBillRequestBody body);

  @POST("payment/credit-cards/pay")
  Observable<Response<PaymentResult>> payCreditCardBill(
    @Header(Api.Header.AUTHORIZATION) String authToken,
    @Body PayCreditCardBillRequestBody body);

  @POST("payment/loans/pay")
  Observable<Response<PaymentResult>> payLoanBill(
    @Header(Api.Header.AUTHORIZATION) String authToken,
    @Body PayLoanBillRequestBody body);

  @POST("payments/remove-invoice")
  Observable<Response<Void>> removeBill(
    @Header(Api.Header.AUTHORIZATION) String authToken,
    @Body BillRequestBody body);

  @POST("payments/validate-pin")
  Observable<Response<Boolean>> validatePin(
    @Header(Api.Header.AUTHORIZATION) String authToken,
    @Body ValidatePinRequestBody body);
}
