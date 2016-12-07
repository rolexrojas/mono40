package com.gbh.movil.data.api;

import android.support.annotation.NonNull;

import com.gbh.movil.misc.Utils;
import com.gbh.movil.domain.Balance;
import com.gbh.movil.domain.Bank;
import com.gbh.movil.domain.InitialData;
import com.gbh.movil.domain.PhoneNumber;
import com.gbh.movil.domain.Product;
import com.gbh.movil.domain.ProductCategory;
import com.gbh.movil.domain.Recipient;
import com.gbh.movil.domain.Transaction;
import com.gbh.movil.domain.api.ApiBridge;
import com.gbh.movil.domain.api.ApiCode;
import com.gbh.movil.domain.api.ApiResult;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * TODO
 *
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
  public Observable<ApiResult<List<Bank>>> banks() {
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
  public Observable<ApiResult<List<Recipient>>> recipients() {
    return Observable.just(ApiResult.<List<Recipient>>create(new ArrayList<Recipient>()));
  }

  @NonNull
  @Override
  public Observable<ApiResult<Boolean>> checkIfAffiliated(@NonNull PhoneNumber phoneNumber) {
    return apiService.checkIfAssociated(phoneNumber.toString())
      .flatMap(new Func1<Response<Void>, Observable<ApiResult<Boolean>>>() {
        @Override
        public Observable<ApiResult<Boolean>> call(Response<Void> response) {
          return Observable.just(ApiResult.create(response.isSuccessful()));
        }
      });
  }

  @NonNull
  @Override
  public Observable<ApiResult<Boolean>> transferTo(@NonNull Product product,
    @NonNull Recipient recipient, @NonNull BigDecimal amount, @NonNull String pin) {
    return apiService.transferTo(new TransferToRequest(product, recipient, amount, pin))
      .flatMap(new Func1<Response<Void>, Observable<ApiResult<Boolean>>>() {
        @Override
        public Observable<ApiResult<Boolean>> call(Response<Void> response) {
          return Observable.just(ApiResult.create(response.isSuccessful()));
        }
      });
  }

  @NonNull
  @Override
  public Observable<Product> setDefaultPaymentOption(@NonNull Product product) {
    return Observable.error(new UnsupportedOperationException());
  }
}
