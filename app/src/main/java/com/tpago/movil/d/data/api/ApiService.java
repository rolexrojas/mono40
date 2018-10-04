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

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author hecvasro
 */
@Deprecated
interface ApiService {

  @GET("initial-load")
  Observable<Response<InitialData>> initialLoad();

  @POST("query/accounts/balance")
  Observable<Response<AccountBalance>> accountBalance(@Body BalanceQueryRequestBody body);

  @POST("query/credit-cards/balance")
  Observable<Response<CreditCardBalance>> creditCardBalance(@Body BalanceQueryRequestBody body);

  @POST("query/loans/balance")
  Observable<Response<LoanBalance>> loanBalance(@Body BalanceQueryRequestBody body);

  @GET("transaction-history")
  Observable<Response<List<Transaction>>> recentTransactions();

  @GET("transfer/recipient-info")
  Observable<Response<Customer>> fetchCustomer(@Query("recipient-msisdn") String phoneNumber);


  @POST("transfer/gcs-gcs")
  Observable<Response<TransferResponseBody>> transferToAffiliated(
    @Body TransferToAffiliatedRequestBody body
  );

  @POST("transfer/gcs-non")
  Observable<Response<TransferResponseBody>> transferToNonAffiliated(
    @Body TransferToNonAffiliatedRequestBody body
  );

  @POST("transfer/gcs-own")
  Observable<Response<TransferResponseBody>> transferTo(@Body TransferToOwnRequestBody body);

  @POST("payments/change-default-account")
  Observable<Response<Void>> setDefaultPaymentOption(@Body Map<String, String> body);

  @POST("transfer/recipient-account-info")
  Observable<Response<ProductInfo>> fetchProductInfo(@Body RecipientAccountInfoRequestBody body);

  @POST("payments/invoices")
  Observable<Response<Void>> addBill(@Body BillRequestBody body);

  @POST("payments/invoices/balance")
  Observable<Response<BillBalance>> queryBalance(@Body BillRequestBody body);

  @POST("payments/credit-cards/balance")
  Observable<Response<CreditCardBillBalance>> queryCreditCardBillBalance(@Body Product body);

  @POST("payments/loans/balance")
  Observable<Response<LoanBillBalance>> queryLoanBalance(@Body Product body);

  @GET("payments/invoices")
  Observable<Response<List<BillResponseBody>>> getBills();

  @POST("payments/invoices/pay")
  Observable<Response<Void>> payBill(@Body PayBillRequestBody body);

  @POST("payments/credit-cards/pay")
  Observable<Response<PaymentResult>> payCreditCardBill(@Body PayCreditCardBillRequestBody body);

  @POST("payments/loans/pay")
  Observable<Response<PaymentResult>> payLoanBill(@Body PayLoanBillRequestBody body);

  @POST("payments/remove-invoice")
  Observable<Response<Void>> removeBill(@Body BillRequestBody body);

  @POST("payments/validate-pin")
  Observable<Response<Boolean>> validatePin(@Body ValidatePinRequestBody body);

  @POST("recharge/direct")
  Observable<Response<TransferResponseBody>> recharge(@Body RechargeRequestBody body);

  @POST("cash-advance")
  Observable<Response<TransferResponseBody>> advanceCash(@Body CashAdvanceRequestBody body);

}
