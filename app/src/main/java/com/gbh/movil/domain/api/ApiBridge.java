package com.gbh.movil.domain.api;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.Balance;
import com.gbh.movil.domain.Bank;
import com.gbh.movil.domain.InitialData;
import com.gbh.movil.domain.PhoneNumber;
import com.gbh.movil.domain.Product;
import com.gbh.movil.domain.Recipient;
import com.gbh.movil.domain.Transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import rx.Observable;

/**
 * TODO
 *
 * @author hecvasro
 */
public interface ApiBridge {
  /**
   * Creates an {@link Observable observable} that emits all the associated {@link Bank banks}.
   * <p>
   * <em>Note:</em> By default {@link #banks()} does not operates on a particular {@link
   * rx.Scheduler}.
   *
   * @return An {@link Observable observable} that emits all the associated {@link Bank banks}.
   */
  @NonNull
  Observable<ApiResult<Set<Bank>>> banks();

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  Observable<ApiResult<InitialData>> initialLoad();

  /**
   * Query the {@link Balance balance} of a {@link Product product} from the API.
   * <p>
   * <em>Note:</em> By default {@link #queryBalance(Product, String)} does not operates on a
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
  Observable<ApiResult<Balance>> queryBalance(@NonNull Product product, @NonNull String pin);

  /**
   * Creates an {@link Observable observable} that emits the latest {@link Transaction transactions}
   * that were made.
   * <p>
   * <em>Note:</em> By default {@link #recentTransactions()} does not operates on a particular
   * {@link rx.Scheduler}.
   *
   * @return An {@link Observable observable} that emits the latest {@link Transaction transactions}
   * that were made.
   */
  @NonNull
  Observable<ApiResult<List<Transaction>>> recentTransactions();

  /**
   * Creates an {@link Observable observable} that emits all the registered {@link Recipient
   * recipients}.
   * <p>
   * <em>Note:</em> By default {@link #recipients()} does not operates on a particular {@link
   * rx.Scheduler}.
   *
   * @return An {@link Observable observable} that emits all the registered {@link Recipient
   * recipients}.
   */
  @NonNull
  Observable<ApiResult<Set<Recipient>>> recipients();

  /**
   * TODO
   *
   * @param phoneNumber
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  Observable<ApiResult<Boolean>> checkIfAffiliated(@NonNull PhoneNumber phoneNumber);

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
  Observable<ApiResult<Boolean>> transferTo(@NonNull Product product, @NonNull Recipient recipient,
    @NonNull BigDecimal amount, @NonNull String pin);
}
