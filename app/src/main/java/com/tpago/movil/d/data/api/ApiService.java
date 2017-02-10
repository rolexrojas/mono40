package com.tpago.movil.d.data.api;

import com.tpago.movil.d.domain.AccountBalance;
import com.tpago.movil.d.domain.CreditCard;
import com.tpago.movil.d.domain.Account;
import com.tpago.movil.d.domain.CreditCardBalance;
import com.tpago.movil.d.domain.InitialData;
import com.tpago.movil.d.domain.Loan;
import com.tpago.movil.d.domain.LoanBalance;
import com.tpago.movil.d.domain.Transaction;

import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * TODO
 *
 * @author hecvasro
 */
interface ApiService {
  @GET("initial-load")
  Observable<Response<InitialData>> initialLoad(@Header(Api.Header.AUTHORIZATION) String authToken);

  @GET("query/accounts")
  Observable<Response<Set<Account>>> accounts();

  @POST("query/accounts/balance")
  Observable<Response<AccountBalance>> accountBalance(
    @Header(Api.Header.AUTHORIZATION) String authToken, @Body BalanceQueryRequest body);

  @GET("query/credit-cards")
  Observable<Response<Set<CreditCard>>> creditCards();

  @POST("query/credit-cards/balance")
  Observable<Response<CreditCardBalance>> creditCardBalance(
    @Header(Api.Header.AUTHORIZATION) String authToken, @Body BalanceQueryRequest body);

  @POST("query/loans")
  Observable<Response<Set<Loan>>> loans();

  @POST("query/loans/balance")
  Observable<Response<LoanBalance>> loanBalance(@Header(Api.Header.AUTHORIZATION) String authToken,
    @Body BalanceQueryRequest body);

  @GET("query/last-transactions")
  Observable<Response<List<Transaction>>> recentTransactions(
    @Header(Api.Header.AUTHORIZATION) String authToken);

  @GET("transfer/recipient-info")
  Observable<Response<Void>> checkIfAssociated(@Header(Api.Header.AUTHORIZATION) String authToken,
    @Query("recipient-msisdn") String phoneNumber);

  @POST("transfer/gcs-gcs")
  Observable<Response<Void>> transferTo(@Header(Api.Header.AUTHORIZATION) String authToken,
    @Body TransferToRequest body);

  @POST("payments/change-default-account")
  Observable<Response<Void>> setDefaultPaymentOption(
    @Header(Api.Header.AUTHORIZATION) String authToken, @Body Map<String, String> body);
}
