package com.tpago.movil.d.data.api;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.tpago.movil.Partner;
import com.tpago.movil.domain.PhoneNumber;
import com.tpago.movil.d.domain.AccountRecipient;
import com.tpago.movil.d.domain.Balance;
import com.tpago.movil.d.domain.CreditCardBillBalance;
import com.tpago.movil.d.domain.Customer;
import com.tpago.movil.d.domain.LoanBillBalance;
import com.tpago.movil.d.domain.PaymentResult;
import com.tpago.movil.d.domain.ProductBillBalance;
import com.tpago.movil.d.domain.ProductRecipient;
import com.tpago.movil.d.domain.Bank;
import com.tpago.movil.d.domain.BillBalance;
import com.tpago.movil.d.domain.BillRecipient;
import com.tpago.movil.d.domain.InitialData;
import com.tpago.movil.d.domain.NonAffiliatedPhoneNumberRecipient;
import com.tpago.movil.d.domain.PhoneNumberRecipient;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.ProductCreator;
import com.tpago.movil.d.domain.ProductInfo;
import com.tpago.movil.d.domain.Recipient;
import com.tpago.movil.d.domain.Transaction;
import com.tpago.movil.d.domain.api.DepApiBridge;
import com.tpago.movil.d.domain.api.ApiCode;
import com.tpago.movil.d.domain.api.ApiError;
import com.tpago.movil.d.domain.api.ApiResult;

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
                return Observable
                  .just(new ApiResult<>(ApiCode.fromValue(response.code()), data, error));
              } catch (IOException exception) {
                return Observable.error(exception);
              }
            }
          });
      }
    };
  }

  private <A, B> Func1<Response<A>, Observable<ApiResult<B>>> mapToApiResult(
    final Func1<A, B> mapFunc
  ) {
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
              errorConverter.convert(response.errorBody())
            ));
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
  public Observable<ApiResult<InitialData>> initialLoad(@NonNull String authToken) {
    return apiService.initialLoad(authToken)
      .compose(this.<InitialData>transformToApiResult());
  }

  /**
   * {@inheritDoc}
   */
  @NonNull
  @Override
  public ApiResult<Balance> queryBalance(
    @NonNull String authToken,
    @NonNull Product product, @NonNull String pin
  ) {
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
    final BalanceQueryRequestBody request = BalanceQueryRequestBody.create(product, pin);
    final Observable<ApiResult<Balance>> observable;
    if (Product.checkIfCreditCard(product)) {
      observable = apiService.creditCardBalance(authToken, request)
        .flatMap(func1);
    } else if (Product.checkIfLoan(product)) {
      observable = apiService.loanBalance(authToken, request)
        .flatMap(func1);
    } else {
      observable = apiService.accountBalance(authToken, request)
        .flatMap(func1);
    }
    return observable
      .toBlocking()
      .single();
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
  public Observable<ApiResult<Boolean>> checkIfAffiliated(
    @NonNull String authToken,
    @NonNull String phoneNumber
  ) {
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
              if (error.getCode()
                .equals(ApiError.Code.UNREGISTERED_PHONE_NUMBER)) {
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
    final String authToken,
    final Product product,
    final Recipient recipient,
    final BigDecimal amount,
    final String pin
  ) {
    if (recipient instanceof AccountRecipient) {
      final AccountRecipient r = (AccountRecipient) recipient;
      return apiService.transferToNonAffiliated(
        authToken,
        TransferToNonAffiliatedRequestBody.create(
          ProductInfo.create(product),
          ProductInfo.create(r.product()),
          pin,
          amount
        )
      )
        .flatMap(mapToApiResult(TransferResponseBody.mapFunc()));
    } else if (recipient instanceof NonAffiliatedPhoneNumberRecipient) {
      final NonAffiliatedPhoneNumberRecipient napnr = (NonAffiliatedPhoneNumberRecipient) recipient;
      return apiService.fetchProductInfo(
        authToken,
        RecipientAccountInfoRequestBody.create(napnr.getBank(), napnr.getAccountNumber())
      )
        .flatMap(mapToApiResult(RetrofitApiBridge.identityMapFunc()))
        .flatMap(new Func1<ApiResult<ProductInfo>, Observable<ApiResult<String>>>() {
          @Override
          public Observable<ApiResult<String>> call(ApiResult<ProductInfo> apiResult) {
            if (apiResult.isSuccessful()) {
              return apiService.transferToNonAffiliated(
                authToken,
                TransferToNonAffiliatedRequestBody.create(
                  ProductInfo.create(product),
                  apiResult.getData(),
                  pin,
                  amount
                )
              )
                .flatMap(mapToApiResult(TransferResponseBody.mapFunc()));
            } else {
              return Observable.just(new ApiResult<String>(
                apiResult.getCode(),
                null,
                apiResult.getError()
              ));
            }
          }
        });
    } else {
      final PhoneNumberRecipient pnr = (PhoneNumberRecipient) recipient;
      return apiService.fetchCustomer(
        authToken,
        pnr.getPhoneNumber()
          .value()
      )
        .flatMap(mapToApiResult(RetrofitApiBridge.<Customer>identityMapFunc()))
        .flatMap(new Func1<ApiResult<Customer>, Observable<ApiResult<String>>>() {
          @Override
          public Observable<ApiResult<String>> call(ApiResult<Customer> result) {
            if (result.isSuccessful()) {
              return apiService.transferToAffiliated(
                authToken,
                TransferToAffiliatedRequestBody.create(
                  product,
                  pnr,
                  result.getData()
                    .getName(),
                  amount,
                  pin
                )
              )
                .flatMap(mapToApiResult(TransferResponseBody.mapFunc()));
            } else {
              return Observable.just(new ApiResult<String>(
                result.getCode(),
                null,
                result.getError()
              ));
            }
          }
        });
    }
  }

  @Override
  public Observable<ApiResult<String>> transferTo(
    String authToken,
    Product fundingProduct,
    Product destinationProduct,
    BigDecimal amount,
    String pin
  ) {
    return apiService.transferTo(
      authToken,
      TransferToOwnRequestBody.builder()
        .fundingProduct(fundingProduct)
        .recipientProduct(destinationProduct)
        .amount(amount)
        .pin(pin)
        .build()
    )
      .flatMap(mapToApiResult(TransferResponseBody.mapFunc()));
  }

  @Override
  public ApiResult<Void> setDefaultPaymentOption(String authToken, Product product) {
    final Map<String, String> body = new HashMap<>();
    body.put("alias", product.getAlias());
    return apiService.setDefaultPaymentOption(authToken, body)
      .flatMap(mapToApiResult(MAP_FUNC_VOID))
      .toBlocking()
      .single();
  }

  @Override
  public Observable<ApiResult<Pair<String, Product>>> checkAccountNumber(
    String authToken,
    Bank bank,
    String accountNumber
  ) {
    return apiService.fetchProductInfo(
      authToken,
      RecipientAccountInfoRequestBody.create(bank, accountNumber)
    )
      .flatMap(mapToApiResult(new Func1<ProductInfo, Pair<String, Product>>() {
        @Override
        public Pair<String, Product> call(ProductInfo productInfo) {
          return Pair.create(
            productInfo.getRecipientName(),
            ProductCreator.create(
              productInfo.getType(),
              productInfo.getAlias(),
              productInfo.getNumber(),
              productInfo.getBank(),
              productInfo.getCurrency(),
              productInfo.getQueryFee(),
              false,
              false,
              null
            )
          );
        }
      }));
  }

  @Override
  public Observable<ApiResult<List<Bank>>> banks() {
    return this.apiService.banks(null)
      .flatMap(mapToApiResult(BankListRequestResponse.mapFunc()));
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
    String pin
  ) {
    return apiService.addBill(
      authToken,
      BillRequestBody.create(partner, contractNumber, pin)
    )
      .flatMap(mapToApiResult(MAP_FUNC_VOID));
  }

  @Override
  public ApiResult<Void> removeBill(String authToken, BillRecipient bill, String pin) {
    return apiService.removeBill(
      authToken,
      BillRequestBody.create(bill.getPartner(), bill.getContractNumber(), pin)
    )
      .flatMap(mapToApiResult(MAP_FUNC_VOID))
      .toBlocking()
      .single();
  }

  @Override
  public ApiResult<BillBalance> queryBalance(
    String authToken,
    BillRecipient recipient
  ) {
    return apiService.queryBalance(
      authToken,
      BillRequestBody.create(recipient.getPartner(), recipient.getContractNumber(), null)
    )
      .flatMap(mapToApiResult(RetrofitApiBridge.<BillBalance>identityMapFunc()))
      .toBlocking()
      .single();
  }

  @Override
  public ApiResult<ProductBillBalance> queryBalance(String authToken, ProductRecipient recipient) {
    final Product product = recipient.getProduct();
    if (Product.checkIfCreditCard(product)) {
      return apiService.queryCreditCardBillBalance(authToken, product)
        .flatMap(mapToApiResult(new Func1<CreditCardBillBalance, ProductBillBalance>() {
          @Override
          public ProductBillBalance call(CreditCardBillBalance productBillBalance) {
            return productBillBalance;
          }
        }))
        .toBlocking()
        .single();
    } else {
      return apiService.queryLoanBalance(authToken, product)
        .flatMap(mapToApiResult(new Func1<LoanBillBalance, ProductBillBalance>() {
          @Override
          public ProductBillBalance call(LoanBillBalance productBillBalance) {
            return productBillBalance;
          }
        }))
        .toBlocking()
        .single();
    }
  }

  @Override
  public Observable<ApiResult<String>> payBill(
    String authToken,
    BillRecipient bill,
    Product fundingAccount,
    BillRecipient.Option option,
    String pin
  ) {
    return apiService.payBill(
      authToken,
      PayBillRequestBody.create(fundingAccount, bill, pin, option)
    )
      .flatMap(mapToApiResult(new Func1<Void, String>() {
        @Override
        public String call(Void aVoid) {
          return Long.toString(System.currentTimeMillis());
        }
      }));
  }

  @Override
  public Observable<ApiResult<PaymentResult>> payCreditCardBill(
    String authToken,
    BigDecimal amountToPay,
    CreditCardBillBalance.Option option,
    String pin,
    Product fundingAccount,
    Product creditCard
  ) {
    final PayCreditCardBillRequestBody body = PayCreditCardBillRequestBody.Builder.create()
      .amountToPay(amountToPay)
      .payOption(option)
      .pin(pin)
      .fundingAccount(ProductInfo.create(fundingAccount))
      .creditCard(creditCard)
      .build();
    return apiService.payCreditCardBill(authToken, body)
      .flatMap(mapToApiResult(RetrofitApiBridge.<PaymentResult>identityMapFunc()));
  }

  @Override
  public Observable<ApiResult<PaymentResult>> payLoanBill(
    String authToken,
    BigDecimal amountToPay,
    LoanBillBalance.Option option,
    String pin,
    Product fundingAccount,
    Product loan
  ) {
    final PayLoanBillRequestBody body = PayLoanBillRequestBody.Builder.create()
      .amountToPay(amountToPay)
      .payOption(option)
      .pin(pin)
      .fundingAccount(ProductInfo.create(fundingAccount))
      .loan(loan)
      .build();
    return apiService.payLoanBill(authToken, body)
      .flatMap(mapToApiResult(RetrofitApiBridge.<PaymentResult>identityMapFunc()));
  }

  @Override
  public ApiResult<Boolean> validatePin(String authToken, String pin) {
    return apiService.validatePin(authToken, ValidatePinRequestBody.create(pin))
      .flatMap(mapToApiResult(RetrofitApiBridge.<Boolean>identityMapFunc()))
      .toBlocking()
      .single();
  }

  @Override
  public ApiResult<Customer.State> fetchCustomerState(String authToken, String phoneNumber) {
    return apiService.fetchCustomerStatus(authToken, phoneNumber)
      .flatMap(mapToApiResult(FetchCustomerStateResponseBody.mapFunc()))
      .toBlocking()
      .single();
  }

  @Override
  public ApiResult<Customer> fetchCustomer(String authToken, String phoneNumber) {
    return apiService.fetchCustomer(authToken, phoneNumber)
      .flatMap(mapToApiResult(RetrofitApiBridge.<Customer>identityMapFunc()))
      .toBlocking()
      .single();
  }

  @Override
  public Observable<ApiResult<String>> recharge(
    String authToken,
    Partner carrier,
    PhoneNumber phoneNumber,
    Product fundingAccount,
    BigDecimal amount,
    String pin
  ) {
    final RechargeRequestBody body = RechargeRequestBody.createBuilder()
      .carrier(carrier)
      .phoneNumber(phoneNumber.value())
      .fundingAccount(ProductInfo.create(fundingAccount))
      .amount(amount)
      .pin(pin)
      .build();
    return apiService.recharge(authToken, body)
      .flatMap(mapToApiResult(TransferResponseBody.mapFunc()));
  }

  @Override
  public ApiResult<String> advanceCash(
    String authToken,
    Product fundingAccount,
    Product recipientAccount,
    BigDecimal amount,
    String pin
  ) {
    final CashAdvanceRequestBody body = CashAdvanceRequestBody.createBuilder()
      .fundingAccount(ProductInfo.create(fundingAccount))
      .recipientAccount(ProductInfo.create(recipientAccount))
      .amount(amount)
      .pin(pin)
      .build();
    return apiService.advanceCash(authToken, body)
      .flatMap(mapToApiResult(TransferResponseBody.mapFunc()))
      .toBlocking()
      .single();
  }
}
