package com.gbh.movil.data.api;

import android.support.annotation.NonNull;

import com.gbh.movil.Utils;
import com.gbh.movil.domain.product.Balance;
import com.gbh.movil.domain.Bank;
import com.gbh.movil.domain.product.CreditCard;
import com.gbh.movil.domain.product.Account;
import com.gbh.movil.domain.InitialData;
import com.gbh.movil.domain.product.Loan;
import com.gbh.movil.domain.product.Product;
import com.gbh.movil.domain.product.ProductCategory;
import com.gbh.movil.domain.Recipient;
import com.gbh.movil.domain.product.transaction.Transaction;
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
  public Observable<ApiResult<Set<Account>>> accounts() {
    return apiService.accounts()
      .compose(this.<Set<Account>>transformToApiResult());
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Observable<ApiResult<Set<CreditCard>>> creditCards() {
    return apiService.creditCards()
      .compose(this.<Set<CreditCard>>transformToApiResult());
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Observable<ApiResult<Set<Loan>>> loans() {
    return apiService.loans()
      .compose(this.<Set<Loan>>transformToApiResult());
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Observable<ApiResult<Balance>> queryBalance(@NonNull Product product,
    @NonNull String pin) {
    final Func1<Response<? extends Balance>, Observable<ApiResult<Balance>>> func1
      = new Func1<Response<? extends Balance>, Observable<ApiResult<Balance>>>() {
      @Override
      public Observable<ApiResult<Balance>> call(Response<? extends Balance> response) {
        final ApiCode code = ApiCode.fromValue(response.code());
        if (Utils.isNotNull(code)) {
          return Observable.just(ApiResult.create((Balance) response.body()));
        } else {
          // TODO: Find or create a suitable exception for these cases.
          return Observable.error(new Exception("Unexpected response code (" + response.code()
            + ")"));
        }
      }
    };
    final ProductCategory category = product.getCategory();
    final BalanceQueryRequest request = new BalanceQueryRequest(product, pin);
    if (category.equals(ProductCategory.ACCOUNT)) {
      return apiService.accountBalance(request).flatMap(func1);
    } else if (category.equals(ProductCategory.CREDIT_CARD)) {
      return apiService.creditCardBalance(request).flatMap(func1);
    } else {
      return apiService.loanBalance(request).flatMap(func1);
    }
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

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Observable<ApiResult<Set<Recipient>>> recipients() {
    return Observable.error(new UnsupportedOperationException());
  }
}
