package com.tpago.movil.d.domain;

import android.support.annotation.NonNull;

import com.tpago.movil.Bank;

import java.util.Set;

import rx.Observable;

/**
 * TODO
 *
 * @author hecvasro
 */
public interface BankRepo {
  /**
   * Creates a {@link Bank bank} and stores it locally.
   * <p>
   * <em>Note:</em> By default {@link #create(String, String, boolean)} does not operates on a
   * particular {@link rx.Scheduler}.
   *
   * @param id
   *   {@link Bank}'s identifier.
   * @param name
   *   {@link Bank}'s name.
   * @param state
   *   {@link Bank}'s state. True if it is active, false otherwise.
   *
   * @return TODO
   */
  @NonNull
  Observable<Bank> create(@NonNull String id, @NonNull String name, boolean state);

  /**
   * Gets all the locally stored {@link Bank banks}.
   * <p>
   * <em>Note:</em> By default {@link #getAll()} does not operates on a particular {@link
   * rx.Scheduler}.
   *
   * @return All the locally stored {@link Bank banks}.
   */
  @NonNull
  Observable<Set<Bank>> getAll();

  /**
   * Gets the {@link Bank bank} identified by the given id.
   * <p>
   * <em>Note:</em> By default {@link #getById(String)} does not operates on a particular {@link
   * rx.Scheduler}.
   *
   * @param id
   *   {@link Bank}'s identifier.
   *
   * @return {@link Bank} identified by the given id.
   */
  @NonNull
  Observable<Bank> getById(@NonNull String id);

  void clear();
}
