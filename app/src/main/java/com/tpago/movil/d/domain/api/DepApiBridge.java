package com.tpago.movil.d.domain.api;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.tpago.movil.Partner;
import com.tpago.movil.d.domain.Balance;
import com.tpago.movil.d.domain.CreditCardBillBalance;
import com.tpago.movil.d.domain.Customer;
import com.tpago.movil.d.domain.LoanBillBalance;
import com.tpago.movil.d.domain.PaymentResult;
import com.tpago.movil.d.domain.PhoneNumber;
import com.tpago.movil.d.domain.ProductBillBalance;
import com.tpago.movil.d.domain.ProductRecipient;
import com.tpago.movil.domain.Bank;
import com.tpago.movil.d.domain.BillBalance;
import com.tpago.movil.d.domain.BillRecipient;
import com.tpago.movil.d.domain.InitialData;
import com.tpago.movil.d.domain.Product;
import com.tpago.movil.d.domain.Recipient;
import com.tpago.movil.d.domain.Transaction;

import java.math.BigDecimal;
import java.util.List;

import rx.Observable;

/**
 * TODO
 *
 * @author hecvasro
 */
@Deprecated
public interface DepApiBridge {
  /**
   * TODO
   *
   * @param authToken
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  Observable<ApiResult<InitialData>> initialLoad(@NonNull String authToken);

  /**
   * Query the {@link Balance balance} of a {@link Product creditCard} from the API.
   * <p>
   * <em>Note:</em> By default {@link #queryBalance(String, Product, String)} does not operates on a
   * particular {@link rx.Scheduler}.
   *
   * @param product
   *   {@link Product} that will be queried.
   * @param pin
   *   User's PIN code.
   *
   * @return {@link Balance balance} of a {@link Product creditCard} from the API.
   */
  @NonNull
  ApiResult<Balance> queryBalance(String authToken, Product product, String pin);

  /**
   * Creates an {@link Observable observable} that emits the latest {@link Transaction transactions}
   * that were made.
   * <p>
   * <em>Note:</em> By default {@link #recentTransactions(String)} does not operates on a particular
   * {@link rx.Scheduler}.
   *
   * @return An {@link Observable observable} that emits the latest {@link Transaction transactions}
   * that were made.
   */
  @NonNull
  Observable<ApiResult<List<Transaction>>> recentTransactions(@NonNull String authToken);

  /**
   * Creates an {@link Observable observable} that emits all the registered {@link Recipient
   * recipients}.
   * <p>
   * <em>Note:</em> By default {@link #recipients(String)} does not operates on a particular {@link
   * rx.Scheduler}.
   *
   * @return An {@link Observable observable} that emits all the registered {@link Recipient
   * recipients}.
   */
  @NonNull
  Observable<ApiResult<List<Recipient>>> recipients(@NonNull String authToken);

  /**
   * TODO
   *
   * @param authToken
   *   TODO
   * @param phoneNumber
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  Observable<ApiResult<Boolean>> checkIfAffiliated(@NonNull String authToken,
    @NonNull String phoneNumber);

  /**
   * TODO
   *
   * @param recipient
   *   TODO
   * @param amount
   *   TODO
   * @param pin
   *   TODO
   *
   * @return TODO
   */
  Observable<ApiResult<String>> transferTo(
    String authToken,
    Product product,
    Recipient recipient,
    BigDecimal amount,
    String pin);

  ApiResult<Void> setDefaultPaymentOption(String authToken, Product paymentOption);

  Observable<ApiResult<Pair<String, Product>>> checkAccountNumber(
    String authToken,
    Bank bank,
    String accountNumber);

  Observable<ApiResult<List<Partner>>> partners(String authToken);

  Observable<ApiResult<Void>> addBill(
    String authToken,
    Partner partner,
    String contractNumber,
    String pin);

  ApiResult<Void> removeBill(
    String authToken,
    BillRecipient bill,
    String pin);

  ApiResult<BillBalance> queryBalance(String authToken, BillRecipient recipient);

  ApiResult<ProductBillBalance> queryBalance(String authToken, ProductRecipient recipient);

  Observable<ApiResult<String>> payBill(
    String authToken,
    BillRecipient bill,
    Product fundingAccount,
    BillRecipient.Option option,
    String pin);

  Observable<ApiResult<PaymentResult>> payCreditCardBill(
    String authToken,
    BigDecimal amountToPay,
    CreditCardBillBalance.Option option,
    String pin,
    Product fundingAccount,
    Product creditCard);

  Observable<ApiResult<PaymentResult>> payLoanBill(
    String authToken,
    BigDecimal amountToPay,
    LoanBillBalance.Option option,
    String pin,
    Product fundingAccount,
    Product loan);

  ApiResult<Boolean> validatePin(String authToken, String pin);

  ApiResult<Customer.State> fetchCustomerState(String authToken, String phoneNumber);
  ApiResult<Customer> fetchCustomer(String authToken, String phoneNumber);
}
