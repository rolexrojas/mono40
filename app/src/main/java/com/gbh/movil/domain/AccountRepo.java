package com.gbh.movil.domain;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import java.util.Set;

import rx.Observable;

/**
 * TODO
 *
 * @author hecvasro
 */
public interface AccountRepo {
  /**
   * Creates an {@link Observable observable} that saves all the given {@link Account accounts}
   * locally, emits them and a {@link Pair pair} of {@link Boolean booleans} that indicates if
   * there're any additions and/or removals compared to what was already saved locally.
   * <p>
   * If any of the given {@link Account accounts} already exists, it will be updated.
   * <p>
   * <em>Note:</em> By default {@link #saveAll(Set)} does not operates on a particular {@link
   * rx.Scheduler}.
   *
   * @param accounts
   *   {@link Account Accounts} that will be saved.
   *
   * @return An {@link Observable observable} that saves all the given {@link Account accounts}
   * locally, emits them and a {@link Pair pair} of {@link Boolean booleans} that indicates if
   * there're any additions and/or removals compared to what was already saved locally.
   */
  @NonNull
  Observable<Pair<Set<Account>, Pair<Boolean, Boolean>>> saveAll(@NonNull Set<Account> accounts);

  /**
   * Creates an {@link Observable observable} that emits all the {@link Account accounts} saved
   * locally.
   * <p>
   * <em>Note:</em> By default {@link #getAll()} does not operates on a particular {@link
   * rx.Scheduler}.
   *
   * @return An {@link Observable observable} that emits all the {@link Account accounts} saved
   * locally.
   */
  @NonNull
  Observable<Set<Account>> getAll();
}
