package com.gbh.movil.data.api;

import android.support.annotation.NonNull;

import com.gbh.movil.Utils;
import com.gbh.movil.domain.Balance;
import com.gbh.movil.domain.Bank;
import com.gbh.movil.domain.CreditCardAccount;
import com.gbh.movil.domain.ElectronicAccount;
import com.gbh.movil.domain.InitialData;
import com.gbh.movil.domain.LoanAccount;
import com.gbh.movil.domain.Recipient;
import com.gbh.movil.domain.Transaction;
import com.gbh.movil.domain.api.ApiBridge;
import com.gbh.movil.domain.api.ApiCode;
import com.gbh.movil.domain.api.ApiResult;

import java.util.List;
import java.util.Set;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

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
  public Observable<ApiResult<Set<ElectronicAccount>>> accounts() {
    return apiService.accounts()
      .compose(this.<Set<ElectronicAccount>>transformToApiResult());
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Observable<ApiResult<Balance>> queryBalance(@NonNull ElectronicAccount account,
    @NonNull String pin) {
    return apiService.accountBalance(new BalanceQueryRequest(account, pin))
      .compose(this.<Balance>transformToApiResult());
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Observable<ApiResult<Set<CreditCardAccount>>> creditCards() {
    return apiService.creditCards()
      .compose(this.<Set<CreditCardAccount>>transformToApiResult());
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Observable<ApiResult<Balance>> queryBalance(@NonNull CreditCardAccount creditCard,
    @NonNull String pin) {
    return apiService.creditCardBalance(new BalanceQueryRequest(creditCard, pin))
      .compose(this.<Balance>transformToApiResult());
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Observable<ApiResult<Set<LoanAccount>>> loans() {
    return apiService.loans()
      .compose(this.<Set<LoanAccount>>transformToApiResult());
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Observable<ApiResult<Balance>> queryBalance(@NonNull LoanAccount loan,
    @NonNull String pin) {
    return apiService.loanBalance(new BalanceQueryRequest(loan, pin))
      .compose(this.<Balance>transformToApiResult());
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
    return apiService.recentTransactions()
      .compose(this.<List<Transaction>>transformToApiResult());
  }
}
