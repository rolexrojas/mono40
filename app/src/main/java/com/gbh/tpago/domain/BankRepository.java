package com.gbh.tpago.domain;

import android.support.annotation.NonNull;

import rx.Observable;

/**
 * TODO
 *
 * @author hecvasro
 */
public interface BankRepository {
  /**
   * TODO
   *
   * @param id
   *   TODO
   * @param name
   *   TODO
   * @param active
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  Observable<Bank> create(@NonNull String id, @NonNull String name, boolean active);

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  Observable<Bank> getAll();

  /**
   * TODO
   *
   * @param id
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  Observable<Bank> getById(@NonNull String id);
}
