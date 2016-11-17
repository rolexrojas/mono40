package com.gbh.movil.domain.api;

import android.support.annotation.NonNull;

import com.gbh.movil.domain.Account;
import com.gbh.movil.domain.Balance;
import com.gbh.movil.domain.Bank;
import com.gbh.movil.domain.CreditCardAccount;
import com.gbh.movil.domain.ElectronicAccount;
import com.gbh.movil.domain.InitialData;
import com.gbh.movil.domain.LoanAccount;
import com.gbh.movil.domain.Recipient;
import com.gbh.movil.domain.Transaction;

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
   * Creates an {@link Observable observable} that emits all the registered {@link ElectronicAccount
   * accounts}.
   * <p>
   * <em>Note:</em> By default {@link #accounts()} does not operates on a particular {@link
   * rx.Scheduler}.
   *
   * @return An {@link Observable observable} that emits all the registered {@link ElectronicAccount
   * accounts}.
   */
  @NonNull
  Observable<ApiResult<Set<ElectronicAccount>>> accounts();

  /**
   * Query the {@link Balance balance} of an {@link ElectronicAccount account} from the API.
   * <p>
   * <em>Note:</em> By default {@link #queryBalance(ElectronicAccount, String)} does not operates on
   * a particular {@link rx.Scheduler}.
   *
   * @param account
   *   {@link ElectronicAccount Account} that will be queried.
   * @param pin
   *   User's PIN code.
   *
   * @return {@link Balance balance} of an {@link ElectronicAccount account} from the API.
   */
  @NonNull
  Observable<ApiResult<Balance>> queryBalance(@NonNull ElectronicAccount account,
    @NonNull String pin);

  /**
   * Creates an {@link Observable observable} that emits all the registered {@link CreditCardAccount
   * credit cards}.
   * <p>
   * <em>Note:</em> By default {@link #creditCards()} does not operates on a particular {@link
   * rx.Scheduler}.
   *
   * @return An {@link Observable observable} that emits all the registered {@link CreditCardAccount
   * credit cards}.
   */
  @NonNull
  Observable<ApiResult<Set<CreditCardAccount>>> creditCards();

  /**
   * Query the {@link Balance balance} of a {@link CreditCardAccount credit card} from the API.
   * <p>
   * <em>Note:</em> By default {@link #queryBalance(CreditCardAccount, String)} does not operates on
   * a particular {@link rx.Scheduler}.
   *
   * @param creditCard
   *   {@link CreditCardAccount Credit card} that will be queried.
   * @param pin
   *   User's PIN code.
   *
   * @return {@link Balance balance} of a {@link CreditCardAccount credit card} from the API.
   */
  @NonNull
  Observable<ApiResult<Balance>> queryBalance(@NonNull CreditCardAccount creditCard,
    @NonNull String pin);

  /**
   * Creates an {@link Observable observable} that emits all the registered {@link LoanAccount
   * loans}.
   * <p>
   * <em>Note:</em> By default {@link #loans()} does not operates on a particular {@link
   * rx.Scheduler}.
   *
   * @return An {@link Observable observable} that emits all the registered {@link LoanAccount
   * loans}.
   */
  @NonNull
  Observable<ApiResult<Set<LoanAccount>>> loans();

  /**
   * Query the {@link Balance balance} of a {@link LoanAccount loan} from the API.
   * <p>
   * <em>Note:</em> By default {@link #queryBalance(LoanAccount, String)} does not operates on a
   * particular {@link rx.Scheduler}.
   *
   * @param loan
   *   {@link LoanAccount Loan} that will be queried.
   * @param pin
   *   User's PIN code.
   *
   * @return {@link Balance balance} of a {@link LoanAccount loan} from the API.
   */
  @NonNull
  Observable<ApiResult<Balance>> queryBalance(@NonNull LoanAccount loan, @NonNull String pin);

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
}
