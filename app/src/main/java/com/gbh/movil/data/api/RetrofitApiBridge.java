package com.gbh.movil.data.api;

import android.support.annotation.NonNull;
import android.util.Pair;

import com.gbh.movil.Utils;
import com.gbh.movil.domain.Account;
import com.gbh.movil.domain.Balance;
import com.gbh.movil.domain.Bank;
import com.gbh.movil.domain.InitialData;
import com.gbh.movil.domain.Recipient;
import com.gbh.movil.domain.Transaction;
import com.gbh.movil.domain.api.ApiBridge;
import com.gbh.movil.domain.api.ApiCode;
import com.gbh.movil.domain.api.ApiResult;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * @author hecvasro
 */

class RetrofitApiBridge implements ApiBridge {
  private final ApiService apiService;

  RetrofitApiBridge(@NonNull ApiService apiService) {
    this.apiService = apiService;
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  private <T> Observable.Transformer<Response<T>, ApiResult<T>> transformToApiResult() {
    return new Observable.Transformer<Response<T>, ApiResult<T>>() {
      @Override
      public Observable<ApiResult<T>> call(Observable<Response<T>> observable) {
        return observable
          .flatMap(new Func1<Response<T>, Observable<ApiResult<T>>>() {
            @Override
            public Observable<ApiResult<T>> call(Response<T> response) {
              final ApiCode code = ApiCode.fromValue(response.code());
              if (Utils.isNotNull(code)) {
                return Observable.just(ApiResult.create(code, response.body()));
              } else {
                // TODO: Find or create a suitable exception for these cases.
                return Observable.error(new Exception("Unexpected response code (" + response.code()
                  + ")"));
              }
            }
          });
      }
    };
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Observable<ApiResult<Set<Bank>>> banks() {
    return Observable.error(new UnsupportedOperationException());
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Observable<ApiResult<InitialData>> initialLoad() {
    return apiService.initialLoad()
      .compose(this.<InitialData>transformToApiResult());
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Observable<ApiResult<Set<Account>>> accounts() {
    return apiService.accounts()
      .compose(this.<List<Account>>transformToApiResult())
      .zipWith(apiService.creditCards()
          .compose(this.<List<Account>>transformToApiResult()),
        new Func2<ApiResult<List<Account>>, ApiResult<List<Account>>,
          Pair<ApiResult<List<Account>>, ApiResult<List<Account>>>>() {
          @Override
          public Pair<ApiResult<List<Account>>, ApiResult<List<Account>>> call(
            ApiResult<List<Account>> accountsResult, ApiResult<List<Account>> creditCardsResult) {
            return Pair.create(accountsResult, creditCardsResult);
          }
        })
      .flatMap(new Func1<Pair<ApiResult<List<Account>>, ApiResult<List<Account>>>,
        Observable<ApiResult<Set<Account>>>>() {
        @Override
        public Observable<ApiResult<Set<Account>>> call(
          Pair<ApiResult<List<Account>>, ApiResult<List<Account>>> pair) {
          final ApiResult<List<Account>> accountsResult = pair.first;
          final ApiResult<List<Account>> creditCardsResult = pair.second;
          if (!accountsResult.isSuccessful()) {
            return Observable.just(ApiResult.<Set<Account>>create(accountsResult.getCode()));
          } else if (Utils.isNull(accountsResult.getData())) { // This is no supposed to happen.
            return Observable.error(new NullPointerException("Result's data is not available"));
          } else if (!creditCardsResult.isSuccessful()) {
            return Observable.just(ApiResult.<Set<Account>>create(creditCardsResult.getCode()));
          } else if (Utils.isNull(creditCardsResult.getData())) { // This is no supposed to happen.
            return Observable.error(new NullPointerException("Result's data is not available"));
          } else {
            final Set<Account> accounts = new HashSet<>();
            accounts.addAll(accountsResult.getData());
            accounts.addAll(creditCardsResult.getData());
            return Observable.just(ApiResult.create(accounts));
          }
        }
      });
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Observable<ApiResult<Balance>> queryBalance(@NonNull Account account,
    @NonNull String pin) {
    return Observable.error(new UnsupportedOperationException());
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Observable<ApiResult<Set<Recipient>>> recipients() {
    return Observable.error(new UnsupportedOperationException());
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Observable<ApiResult<List<Transaction>>> recentTransactions() {
    return Observable.error(new UnsupportedOperationException());
  }
}
