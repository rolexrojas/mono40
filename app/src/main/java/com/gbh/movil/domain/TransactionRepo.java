package com.gbh.movil.domain;

import android.support.annotation.NonNull;

import java.util.List;

import rx.Observable;

/**
 * TODO
 *
 * @author hecvasro
 */
public interface TransactionRepo {
  /**
   * Creates an {@link Observable observable} that saves all the given {@link Transaction
   * transactions} locally and emits them ordered from newer to older.
   * <p>
   * All the previously saved {@link Transaction transactions} will be deleted.
   * <p>
   * <em>Note:</em> By default {@link #saveAll(List)} does not operates on a particular {@link
   * rx.Scheduler}.
   *
   * @return An {@link Observable observable} that saves all the given {@link Transaction
   * transactions} locally and emits them ordered from newer to older.
   */
  @NonNull
  Observable<List<Transaction>> saveAll(@NonNull List<Transaction> transactions);

  /**
   * Creates an {@link Observable observable} that emits all the {@link Transaction transactions}
   * saved locally ordered from newer to older.
   * <p>
   * <em>Note:</em> By default {@link #getAll()} does not operates on a particular {@link
   * rx.Scheduler}.
   *
   * @return An {@link Observable observable} that emits all the {@link Transaction transactions}
   * saved locally ordered from newer to older.
   */
  @NonNull
  Observable<List<Transaction>> getAll();
}
