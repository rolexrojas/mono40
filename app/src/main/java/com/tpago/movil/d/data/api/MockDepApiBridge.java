package com.tpago.movil.d.data.api;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.tpago.movil.d.domain.AccountBalance;
import com.tpago.movil.d.domain.Balance;
import com.tpago.movil.d.domain.Bank;
import com.tpago.movil.d.domain.BillBalance;
import com.tpago.movil.d.domain.BillRecipient;
import com.tpago.movil.d.domain.CreditCardBalance;
import com.tpago.movil.d.domain.CreditCardBillBalance;
import com.tpago.movil.d.domain.Customer;
import com.tpago.movil.d.domain.InitialData;
import com.tpago.movil.d.domain.LoanBalance;
import com.tpago.movil.d.domain.LoanBillBalance;
import com.tpago.movil.d.domain.PaymentResult;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.ProductBillBalance;
import com.tpago.movil.d.domain.ProductCreator;
import com.tpago.movil.d.domain.ProductRecipient;
import com.tpago.movil.d.domain.ProductType;
import com.tpago.movil.d.domain.Recipient;
import com.tpago.movil.d.domain.Transaction;
import com.tpago.movil.d.domain.api.ApiCode;
import com.tpago.movil.d.domain.api.ApiResult;
import com.tpago.movil.d.domain.api.DepApiBridge;
import com.tpago.movil.dep.MockData;
import com.tpago.movil.dep.Partner;
import com.tpago.movil.domain.PhoneNumber;
import com.tpago.movil.util.DigitHelper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import rx.Observable;

final class MockDepApiBridge implements DepApiBridge {

  private static final String TRANSACTION_ID = UUID.randomUUID()
    .toString();

  static MockDepApiBridge create() {
    return new MockDepApiBridge();
  }

  private MockDepApiBridge() {
  }

  @NonNull
  @Override
  public Observable<ApiResult<InitialData>> initialLoad(@NonNull String authToken) {
    return Observable.just(
      new ApiResult<>(
        ApiCode.OK,
        new InitialData(
          new ArrayList(MockData.PRODUCT_SET),
          new ArrayList<>()
        ),
        null
      )
    )
      .delay(1L, TimeUnit.SECONDS);
  }

  @NonNull
  @Override
  public ApiResult<Balance> queryBalance(
    String authToken,
    Product product,
    String pin
  ) {
    final Balance balance;
    if (Product.checkIfAccount(product)) {
      balance = new AccountBalance(BigDecimal.valueOf(5000), BigDecimal.valueOf(3500));
    } else if (Product.checkIfCreditCard(product)) {
      balance = new CreditCardBalance(BigDecimal.valueOf(45000), BigDecimal.valueOf(18943.23));
    } else {
      balance = new LoanBalance(BigDecimal.valueOf(150000), BigDecimal.valueOf(3543.87));
    }
    return new ApiResult<>(ApiCode.OK, balance, null);
  }

  @NonNull
  @Override
  public Observable<ApiResult<List<Transaction>>> recentTransactions(@NonNull String authToken) {
    return Observable.just(
      new ApiResult<>(
        ApiCode.OK,
        MockData.TRANSACTION_LIST,
        null
      )
    )
      .delay(1L, TimeUnit.SECONDS);
  }

  @NonNull
  @Override
  public Observable<ApiResult<List<Recipient>>> recipients(@NonNull String authToken) {
    return Observable.just(
      new ApiResult<>(
        ApiCode.OK,
        new ArrayList<>(),
        null
      )
    );
  }

  @NonNull
  @Override
  public Observable<ApiResult<Boolean>> checkIfAffiliated(
    @NonNull String authToken,
    @NonNull String phoneNumber
  ) {
    final List<Integer> digitList = DigitHelper.toDigitList(phoneNumber);
    final int digitListSize = digitList.size();
    final boolean flag = digitList.get(digitListSize - 1) % 2 == 0;
    return Observable.just(
      new ApiResult<>(
        ApiCode.OK,
        flag,
        null
      )
    )
      .delay(1L, TimeUnit.SECONDS);
  }

  @Override
  public Observable<ApiResult<String>> transferTo(
    String authToken, Product product, Recipient recipient, BigDecimal amount, String pin
  ) {
    return Observable.just(
      new ApiResult<>(
        ApiCode.OK,
        TRANSACTION_ID,
        null
      )
    )
      .delay(1L, TimeUnit.SECONDS);
  }

  @Override
  public Observable<ApiResult<String>> transferTo(
    String authToken,
    Product fundingProduct,
    Product destinationProduct,
    BigDecimal amount,
    String pin
  ) {
    return Observable.just(
      new ApiResult<>(
        ApiCode.OK,
        TRANSACTION_ID,
        null
      )
    )
      .delay(1L, TimeUnit.SECONDS);
  }

  @Override
  public ApiResult<Void> setDefaultPaymentOption(
    String authToken, Product paymentOption
  ) {
    return new ApiResult<>(ApiCode.OK, null, null);
  }

  @Override
  public Observable<ApiResult<Pair<String, Product>>> checkAccountNumber(
    String authToken, Bank bank, String accountNumber
  ) {
    return Observable.just(
      new ApiResult<>(
        ApiCode.OK,
        Pair.create(
          "Nelson Mandela",
          ProductCreator.create(
            ProductType.SAV,
            accountNumber,
            "*****" + accountNumber,
            bank,
            "DOP",
            BigDecimal.valueOf(5),
            false,
            false,
            null
          )
        ),
        null
      )
    );
  }

  @Override
  public Observable<ApiResult<List<Bank>>> banks() {
    final List<Bank> bankList = new ArrayList<>(MockData.BANK_SET);
    return Observable.just(new ApiResult<>(ApiCode.OK, bankList, null))
      .delay(1L, TimeUnit.SECONDS);
  }

  @Override
  public Observable<ApiResult<List<Partner>>> partners(String authToken) {
    final List<Partner> partnerList = new ArrayList<>(MockData.PARTNER_SET);
    return Observable.just(new ApiResult<>(ApiCode.OK, partnerList, null))
      .delay(1L, TimeUnit.SECONDS);
  }

  @Override
  public Observable<ApiResult<Void>> addBill(
    String authToken, Partner partner, String contractNumber, String pin
  ) {
    return Observable.just(new ApiResult<>(ApiCode.OK, null, null));
  }

  @Override
  public ApiResult<Void> removeBill(
    String authToken, BillRecipient bill, String pin
  ) {
    return new ApiResult<>(ApiCode.OK, null, null);
  }

  @Override
  public ApiResult<BillBalance> queryBalance(
    String authToken, BillRecipient recipient
  ) {
    final BillBalance balance = BillBalance.builder()
      .setDate("27/09/2017")
      .setTotal(BigDecimal.valueOf(1825 * 2))
      .setMinimum(BigDecimal.valueOf(1825))
      .build();
    return new ApiResult<>(ApiCode.OK, balance, null);
  }

  @Override
  public ApiResult<ProductBillBalance> queryBalance(
    String authToken, ProductRecipient recipient
  ) {
    final Product product = recipient.getProduct();
    final ProductBillBalance balance;
    if (Product.checkIfCreditCard(product)) {
      balance = CreditCardBillBalance.builder()
        .dueDate("27/09/2017")
        .currentAmount(BigDecimal.valueOf(26056.77))
        .periodAmount(BigDecimal.valueOf(26056.77))
        .minimumAmount(
          BigDecimal.valueOf(26056.77)
            .multiply(BigDecimal.valueOf(0.05))
        )
        .build();
    } else {
      balance = LoanBillBalance.builder()
        .dueDate("27/09/2017")
        .currentAmount(BigDecimal.valueOf(150000))
        .periodAmount(BigDecimal.valueOf(3543.87))
        .build();
    }
    return new ApiResult<>(ApiCode.OK, balance, null);
  }

  @Override
  public Observable<ApiResult<String>> payBill(
    String authToken,
    BillRecipient bill,
    Product fundingAccount,
    BillRecipient.Option option,
    String pin
  ) {
    return Observable.just(
      new ApiResult<>(
        ApiCode.OK,
        TRANSACTION_ID,
        null
      )
    )
      .delay(1L, TimeUnit.SECONDS);
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
    return Observable.just(
      new ApiResult<>(
        ApiCode.OK,
        PaymentResult.create(TRANSACTION_ID, TRANSACTION_ID),
        null
      )
    )
      .delay(1L, TimeUnit.SECONDS);
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
    return Observable.just(
      new ApiResult<>(
        ApiCode.OK,
        PaymentResult.create(TRANSACTION_ID, TRANSACTION_ID),
        null
      )
    )
      .delay(1L, TimeUnit.SECONDS);
  }

  @Override
  public ApiResult<Boolean> validatePin(String authToken, String pin) {
    return new ApiResult<>(ApiCode.OK, true, null);
  }

  @Override
  public ApiResult<Customer.State> fetchCustomerState(
    String authToken, String phoneNumber
  ) {
    final List<Integer> digitList = DigitHelper.toDigitList(phoneNumber);
    final int digitListSize = digitList.size();
    final Customer.State state;
    if (digitList.get(digitListSize - 1) % 2 == 0) {
      if (digitList.get(digitListSize - 2) % 2 == 0) {
        state = Customer.State.REGISTERED;
      } else {
        state = Customer.State.AFFILIATED;
      }
    } else {
      state = Customer.State.NONE;
    }
    return new ApiResult<>(ApiCode.OK, state, null);
  }

  @Override
  public ApiResult<Customer> fetchCustomer(String authToken, String phoneNumber) {
    return new ApiResult<>(ApiCode.OK, Customer.create("Nelson Mandela"), null);
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
    return Observable.just(
      new ApiResult<>(
        ApiCode.OK,
        TRANSACTION_ID,
        null
      )
    )
      .delay(1L, TimeUnit.SECONDS);
  }

  @Override
  public ApiResult<String> advanceCash(
    String authToken,
    Product fundingAccount,
    Product recipientAccount,
    BigDecimal amount,
    String pin
  ) {
    return new ApiResult<>(
      ApiCode.OK,
      TRANSACTION_ID,
      null
    );
  }
}
