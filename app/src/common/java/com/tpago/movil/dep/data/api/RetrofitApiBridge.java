package com.tpago.movil.dep.data.api;

import android.support.annotation.NonNull;

import com.tpago.movil.Partner;
import com.tpago.movil.dep.domain.Balance;
import com.tpago.movil.Bank;
import com.tpago.movil.dep.domain.BillBalance;
import com.tpago.movil.dep.domain.BillRecipient;
import com.tpago.movil.dep.domain.InitialData;
import com.tpago.movil.dep.domain.NonAffiliatedPhoneNumberRecipient;
import com.tpago.movil.dep.domain.PhoneNumberRecipient;
import com.tpago.movil.dep.domain.Product;
import com.tpago.movil.dep.domain.ProductCategory;
import com.tpago.movil.dep.domain.Recipient;
import com.tpago.movil.dep.domain.Transaction;
import com.tpago.movil.dep.domain.api.DepApiBridge;
import com.tpago.movil.dep.domain.api.ApiCode;
import com.tpago.movil.dep.domain.api.ApiError;
import com.tpago.movil.dep.domain.api.ApiResult;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author hecvasro
 */
@Deprecated
class RetrofitApiBridge implements DepApiBridge {
  private static final Func1<Void, Void> MAP_FUNC_VOID = new Func1<Void, Void>() {
    @Override
    public Void call(Void aVoid) {
      return aVoid;
    }
  };

  private final ApiService apiService;
  private final Converter<ResponseBody, ApiError> errorConverter;

  private static <T> Func1<T, T> identityMapFunc() {
    return new Func1<T, T>() {
      @Override
      public T call(T t) {
        return t;
      }
    };
  }

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

  private <A, B> Func1<Response<A>, Observable<ApiResult<B>>> mapToApiResult(
    final Func1<A, B> mapFunc) {
    return new Func1<Response<A>, Observable<ApiResult<B>>>() {
      @Override
      public Observable<ApiResult<B>> call(Response<A> response) {
        if (response.isSuccessful()) {
          return Observable.just(new ApiResult<>(ApiCode.OK, mapFunc.call(response.body()), null));
        } else {
          try {
            return Observable.just(new ApiResult<B>(
              ApiCode.fromValue(response.code()),
              null,
              errorConverter.convert(response.errorBody())));
          } catch (IOException exception) {
            return Observable.error(exception);
          }
        }
      }
    };
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public Observable<ApiResult<List<Bank>>> banks(@NonNull String authToken) {
    return apiService.banks(authToken)
      .flatMap(mapToApiResult(BankListRequestResponse.mapFunc()));
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
    final BalanceQueryRequestBody request = BalanceQueryRequestBody.create(product, pin);
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
    return apiService.getBills(authToken)
      .flatMap(mapToApiResult(BillResponseBody.mapFunc()));
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
          if (response.isSuccessful()) {
            return Observable.just(new ApiResult<>(code, true, null));
          } else {
            try {
              final ApiError error = errorConverter.convert(response.errorBody());
              if (error.getCode().equals(ApiError.Code.UNREGISTERED_PHONE_NUMBER)) {
                return Observable.just(new ApiResult<>(ApiCode.OK, false, null));
              } else {
                return Observable.just(new ApiResult<>(code, false, error));
              }
            } catch (IOException exception) {
              return Observable.error(exception);
            }
          }
        }
      });
  }

  @NonNull
  @Override
  public Observable<ApiResult<String>> transferTo(
    @NonNull String authToken,
    @NonNull Product product,
    @NonNull Recipient recipient,
    @NonNull BigDecimal amount,
    @NonNull String pin) {
    final Observable<Response<TransferResponseBody>> observable;
    if (recipient instanceof NonAffiliatedPhoneNumberRecipient) {
      observable = apiService.transferToNonAffiliated(
        authToken,
        TransferToNonAffiliatedRequestBody.create(
          product,
          ((NonAffiliatedPhoneNumberRecipient) recipient),
          pin,
          amount));
    } else {
      observable = apiService.transferToAffiliated(
        authToken,
        TransferToAffiliatedRequestBody.create(
          product,
          (PhoneNumberRecipient) recipient,
          amount,
          pin));
    }
    return observable.flatMap(mapToApiResult(TransferResponseBody.mapFunc()));
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

  @Override
  public Observable<ApiResult<Product>> checkAccountNumber(
    String authToken,
    Bank bank,
    String accountNumber) {
    return apiService.checkAccountNumber(
      authToken,
      RecipientAccountInfoRequestBody.create(bank, accountNumber))
      .flatMap(mapToApiResult(new Func1<Product, Product>() {
        @Override
        public Product call(Product product) {
          return product;
        }
      }));
  }

  @Override
  public Observable<ApiResult<List<Partner>>> partners(String authToken) {
    return apiService.partners(authToken)
      .flatMap(mapToApiResult(PartnerListRequestResponse.mapFunc()));
  }

  @Override
  public Observable<ApiResult<Void>> addBill(
    String authToken,
    Partner partner,
    String contractNumber,
    String pin) {
    return apiService.addBill(
      authToken,
      BillRequestBody.create(partner, contractNumber, pin))
      .flatMap(mapToApiResult(MAP_FUNC_VOID));
  }

  @Override
  public Observable<ApiResult<Recipient>> removeBill(
    String authToken,
    final BillRecipient bill,
    String pin) {
    return apiService.removeBill(
      authToken,
      BillRequestBody.create(bill.getPartner(), bill.getContractNumber(), pin))
      .flatMap(mapToApiResult(new Func1<Void, Recipient>() {
        @Override
        public Recipient call(Void aVoid) {
          return bill;
        }
      }));
  }

  @Override
  public Observable<Recipient> queryBalance(
    String authToken,
    final BillRecipient recipient) {
    return apiService.queryBalance(
      authToken,
      BillRequestBody.create(recipient.getPartner(), recipient.getContractNumber(), null))
      .flatMap(mapToApiResult(RetrofitApiBridge.<BillBalance>identityMapFunc()))
      .map(new Func1<ApiResult<BillBalance>, Recipient>() {
        @Override
        public Recipient call(ApiResult<BillBalance> result) {
          if (result.isSuccessful()) {
            recipient.setBalance(result.getData());
          }
          return recipient;
        }
      });
  }

  @Override
  public Observable<ApiResult<String>> payBill(
    String authToken,
    BillRecipient bill,
    Product fundingAccount,
    BillRecipient.Option option,
    String pin) {
    return apiService.payBill(
      authToken,
      PayBillRequestBody.create(fundingAccount, bill, pin, option))
      .flatMap(mapToApiResult(new Func1<Void, String>() {
        @Override
        public String call(Void aVoid) {
          return Long.toString(System.currentTimeMillis());
        }
      }));
  }
}
