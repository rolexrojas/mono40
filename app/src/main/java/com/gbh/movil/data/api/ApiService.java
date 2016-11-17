package com.gbh.movil.data.api;

import com.gbh.movil.domain.Account;
import com.gbh.movil.domain.Balance;
import com.gbh.movil.domain.InitialData;
import com.gbh.movil.domain.Transaction;

import java.util.List;

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
  Observable<Response<List<Account>>> accounts();

  @POST("query/accounts/balance")
  Observable<Response<Balance>> accountBalance(@Body BalanceQueryRequest body);

  @POST("query/credit-cards/balance")
  Observable<Response<Balance>> creditCardBalance(@Body BalanceQueryRequest body);

  @GET("query/credit-cards")
  Observable<Response<List<Account>>> creditCards();

  @GET("query/last-transactions")
  Observable<Response<List<Transaction>>> recentTransactions();
}
