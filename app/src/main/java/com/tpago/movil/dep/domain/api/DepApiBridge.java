package com.tpago.movil.dep.domain.api;

import android.support.annotation.NonNull;

import com.tpago.movil.Partner;
import com.tpago.movil.dep.domain.Balance;
import com.tpago.movil.Bank;
import com.tpago.movil.dep.domain.BillRecipient;
import com.tpago.movil.dep.domain.InitialData;
import com.tpago.movil.dep.domain.Product;
import com.tpago.movil.dep.domain.Recipient;
import com.tpago.movil.dep.domain.Transaction;

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
   * Creates an {@link Observable observable} that emits all the associated {@link Bank banks}.
   * <p>
   * <em>Note:</em> By default {@link #banks()} does not operates on a particular {@link
   * rx.Scheduler}.
   *
   * @return An {@link Observable observable} that emits all the associated {@link Bank banks}.
   */
  @NonNull
  Observable<ApiResult<List<Bank>>> banks(@NonNull String authToken);

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
   * Query the {@link Balance balance} of a {@link Product product} from the API.
   * <p>
   * <em>Note:</em> By default {@link #queryBalance(String, Product, String)} does not operates on a
   * particular {@link rx.Scheduler}.
   *
   * @param product
   *   {@link Product} that will be queried.
   * @param pin
   *   User's PIN code.
   *
   * @return {@link Balance balance} of a {@link Product product} from the API.
   */
  @NonNull
  Observable<ApiResult<Balance>> queryBalance(@NonNull String authToken, @NonNull Product product,
    @NonNull String pin);

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
  @NonNull
  Observable<ApiResult<String>> transferTo(
    @NonNull String authToken,
    @NonNull Product product,
    @NonNull Recipient recipient,
    @NonNull BigDecimal amount,
    @NonNull String pin);

  /**
   * TODO
   *
   * @param product
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  Observable<ApiResult<Void>> setDefaultPaymentOption(@NonNull String authToken,
    @NonNull Product product);

  Observable<ApiResult<Product>> checkAccountNumber(
    String authToken,
    Bank bank,
    String accountNumber);

  Observable<ApiResult<List<Partner>>> partners(String authToken);

  Observable<ApiResult<Void>> addBill(
    String authToken,
    Partner partner,
    String contractNumber,
    String pin);

  Observable<ApiResult<Recipient>> removeBill(
    String authToken,
    BillRecipient bill,
    String pin);

  Observable<Recipient> queryBalance(String authToken, BillRecipient recipient);

  Observable<ApiResult<String>> payBill(
    String authToken,
    BillRecipient bill,
    Product fundingAccount,
    BillRecipient.Option option,
    String pin);
}
