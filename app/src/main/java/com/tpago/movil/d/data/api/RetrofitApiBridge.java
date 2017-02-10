package com.tpago.movil.d.data.api;

import android.support.annotation.NonNull;

import com.tpago.movil.d.domain.Balance;
import com.tpago.movil.d.domain.Bank;
import com.tpago.movil.d.domain.InitialData;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.ProductCategory;
import com.tpago.movil.d.domain.Recipient;
import com.tpago.movil.d.domain.Transaction;
import com.tpago.movil.d.domain.api.ApiBridge;
import com.tpago.movil.d.domain.api.ApiCode;
import com.tpago.movil.d.domain.api.ApiError;
import com.tpago.movil.d.domain.api.ApiResult;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Converter;
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
  private final Converter<ResponseBody, ApiError> errorConverter;

  RetrofitApiBridge(ApiService apiService, Converter<ResponseBody, ApiError> errorConverter) {
    this.apiService = apiService;
    this.errorConverter = errorConverter;
  }

  @NonNull
  private <T> Observable.Transformer<Response<T>, ApiResult<T>> transformToApiResult() {
    return new Observable.Transformer<Response<T>, ApiResult<T>>() {
      @Override
      public Observable<ApiResult<T>> call(Observable<Response<T>> observable) {
        return observable
          .flatMap(new Func1<Response<T>, Observable<ApiResult<T>>>() {
            @Override
            public Observable<ApiResult<T>> call(Response<T> response) {
              try {
                T data = null;
                ApiError error = null;
                if (response.isSuccessful()) {
                  data = response.body();
                } else {
                  error = errorConverter.convert(response.errorBody());
                }
                return Observable.just(new ApiResult<>(ApiCode.fromValue(response.code()), data, error));
              } catch (IOException exception) {
                return Observable.error(exception);
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
  public Observable<ApiResult<InitialData>> initialLoad(@NonNull String authToken) {
    return apiService.initialLoad(authToken)
      .compose(this.<InitialData>transformToApiResult());
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Observable<ApiResult<Balance>> queryBalance(@NonNull String authToken,
    @NonNull Product product, @NonNull String pin) {
    final Func1<Response<? extends Balance>, Observable<ApiResult<Balance>>> func1
      = new Func1<Response<? extends Balance>, Observable<ApiResult<Balance>>>() {
      @Override
      public Observable<ApiResult<Balance>> call(Response<? extends Balance> response) {
        try {
          Balance data = null;
          ApiError error = null;
          if (response.isSuccessful()) {
            data = response.body();
          } else {
            error = errorConverter.convert(response.errorBody());
          }
          return Observable.just(new ApiResult<>(ApiCode.fromValue(response.code()), data, error));
        } catch (IOException exception) {
          return Observable.error(exception);
        }
      }
    };
    final ProductCategory category = product.getCategory();
    final BalanceQueryRequest request = new BalanceQueryRequest(product, pin);
    if (category.equals(ProductCategory.ACCOUNT)) {
      return apiService.accountBalance(authToken, request).flatMap(func1);
    } else if (category.equals(ProductCategory.CREDIT_CARD)) {
      return apiService.creditCardBalance(authToken, request).flatMap(func1);
    } else {
      return apiService.loanBalance(authToken, request).flatMap(func1);
    }
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Observable<ApiResult<List<Transaction>>> recentTransactions(@NonNull String authToken) {
    return apiService.recentTransactions(authToken)
      .compose(this.<List<Transaction>>transformToApiResult());
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Observable<ApiResult<List<Recipient>>> recipients(@NonNull String authToken) {
    return Observable.just(new ApiResult<List<Recipient>>(ApiCode.OK, new ArrayList<Recipient>(), null));
  }

  @NonNull
  @Override
  public Observable<ApiResult<Boolean>> checkIfAffiliated(@NonNull String authToken,
    @NonNull String phoneNumber) {
    return apiService.checkIfAssociated(authToken, phoneNumber)
      .flatMap(new Func1<Response<Void>, Observable<ApiResult<Boolean>>>() {
        @Override
        public Observable<ApiResult<Boolean>> call(Response<Void> response) {
          final ApiCode code = ApiCode.fromValue(response.code());
          return Observable.just(new ApiResult<>(code, code.equals(ApiCode.OK), null));
        }
      });
  }

  @NonNull
  @Override
  public Observable<ApiResult<Boolean>> transferTo(@NonNull String authToken,
    @NonNull Product product, @NonNull Recipient recipient, @NonNull BigDecimal amount,
    @NonNull String pin) {
    return apiService.transferTo(authToken, new TransferToRequest(product, recipient, amount, pin))
      .flatMap(new Func1<Response<Void>, Observable<ApiResult<Boolean>>>() {
        @Override
        public Observable<ApiResult<Boolean>> call(Response<Void> response) {
          final ApiCode code = ApiCode.fromValue(response.code());
          return Observable.just(new ApiResult<>(code, code.equals(ApiCode.OK), null));
        }
      });
  }

  @NonNull
  @Override
  public Observable<ApiResult<Void>> setDefaultPaymentOption(@NonNull String authToken,
    @NonNull Product product) {
    final Map<String, String> body = new HashMap<>();
    body.put("alias", product.getAlias());
    return apiService.setDefaultPaymentOption(authToken, body)
      .compose(this.<Void>transformToApiResult());
  }
}
