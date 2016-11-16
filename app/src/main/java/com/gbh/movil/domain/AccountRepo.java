package com.gbh.movil.domain;

import android.support.annotation.NonNull;

import rx.Observable;

/**
 * TODO
 *
 * @author hecvasro
 */
public interface AccountRepo extends AccountProvider {
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
  Observable<Account> remove(@NonNull Account account);
}
