package com.gbh.movil.data.api;

import com.gbh.movil.domain.AccountBalance;
import com.gbh.movil.domain.CreditCard;
import com.gbh.movil.domain.Account;
import com.gbh.movil.domain.CreditCardBalance;
import com.gbh.movil.domain.InitialData;
import com.gbh.movil.domain.Loan;
import com.gbh.movil.domain.LoanBalance;
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
  Observable<Response<Set<Account>>> accounts();

  @POST("query/accounts/balance")
  Observable<Response<AccountBalance>> accountBalance(@Body BalanceQueryRequest body);

  @GET("query/credit-cards")
  Observable<Response<Set<CreditCard>>> creditCards();

  @POST("query/credit-cards/balance")
  Observable<Response<CreditCardBalance>> creditCardBalance(@Body BalanceQueryRequest body);

  @POST("query/loans")
  Observable<Response<Set<Loan>>> loans();

  @POST("query/loans/balance")
  Observable<Response<LoanBalance>> loanBalance(@Body BalanceQueryRequest body);

  @GET("query/last-transactions")
  Observable<Response<List<Transaction>>> recentTransactions();
}
