package com.gbh.movil.domain;

import android.support.annotation.NonNull;

import java.util.Set;

import rx.Observable;

/**
 * Contract that defines all the methods required for managing {@link Account accounts} locally.
 *
 * @author hecvasro
 */
public interface AccountRepository {
  /**
   * Gets all the locally stored {@link Account accounts}.
   *
   * @return All the locally stored {@link Account accounts}.
   */
  @NonNull
  Observable<Set<Account>> getAll();

  /**
   * Saves the given {@link Account account} locally. It will be created if it doesn't exists.
   *
   * @param account
   *   {@link Account} that will be saved.
   */
  void save(@NonNull Account account);

  /**
   * Deletes the given {@link Account account} from the local storage.
   *
   * @param account
   *   {@link Account} that will be deleted.
   */
  void delete(@NonNull Account account);
}
