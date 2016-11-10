package com.gbh.movil.domain;

import android.support.annotation.NonNull;

import java.util.Set;

import rx.Observable;

/**
 * TODO
 *
 * @author hecvasro
 */
public interface AccountRepo {
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

  /**
   * TODO
   *
   * @param account
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  Observable<Account> save(@NonNull Account account);

  /**
   * TODO
   *
   * @param account
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  Observable<Boolean> remove(@NonNull Account account);
}
