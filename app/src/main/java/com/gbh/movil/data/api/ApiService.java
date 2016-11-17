package com.gbh.movil.data.api;

import com.gbh.movil.domain.Balance;
import com.gbh.movil.domain.CreditCardAccount;
import com.gbh.movil.domain.ElectronicAccount;
import com.gbh.movil.domain.InitialData;
import com.gbh.movil.domain.LoanAccount;
import com.gbh.movil.domain.Transaction;

import java.util.List;
import java.util.Set;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * TODO
 *
 * @author hecvasro
 */
interface ApiService {
  @GET("initial-load")
  Observable<Response<InitialData>> initialLoad();

  @GET("query/accounts")
  Observable<Response<Set<ElectronicAccount>>> accounts();

  @POST("query/accounts/balance")
  Observable<Response<Balance>> accountBalance(@Body BalanceQueryRequest body);

  @GET("query/credit-cards")
  Observable<Response<Set<CreditCardAccount>>> creditCards();

  @POST("query/credit-cards/balance")
  Observable<Response<Balance>> creditCardBalance(@Body BalanceQueryRequest body);

  @POST("query/loans")
  Observable<Response<Set<LoanAccount>>> loans();

  @POST("query/loan/balance")
  Observable<Response<Balance>> loanBalance(@Body BalanceQueryRequest body);

  @GET("query/last-transactions")
  Observable<Response<List<Transaction>>> recentTransactions();
}
