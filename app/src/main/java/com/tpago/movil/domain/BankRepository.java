package com.tpago.movil.domain;

import android.support.annotation.NonNull;

import java.util.Set;

import rx.Observable;

/**
 * Contract that defines all the required methods for managing {@link Bank banks} locally.
 *
 * @author hecvasro
 */
public interface BankRepository {
  /**
   * Creates a {@link Bank bank} and stores it locally.
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
   *
   * @return All the locally stored {@link Bank banks}.
   */
  @NonNull
  Observable<Set<Bank>> getAll();

  /**
   * Gets the {@link Bank bank} identified by the given id.
   *
   * @param id
   *   {@link Bank}'s identifier.
   *
   * @return {@link Bank} identified by the given id.
   */
  @NonNull
  Observable<Bank> getById(@NonNull String id);
}
