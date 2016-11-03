package com.gbh.movil.domain;

import android.support.annotation.NonNull;

import java.util.Set;

import rx.Observable;

/**
 * TODO
 *
 * @author hecvasro
 */
public interface RecipientRepo {
  /**
   * Creates an {@link Observable observable} that saves all the given {@link Recipient recipients}
   * locally, emits them and all the {@link Recipient recipients} that are only saved locally.
   * <p>
   * If any of the given {@link Recipient recipients} already exists, it will be updated.
   * <p>
   * <em>Note:</em> By default {@link #saveAll(Set)} does not operates on a particular {@link
   * rx.Scheduler}.
   *
   * @param recipients
   *   {@link Recipient Recipients} that will be created or updated.
   *
   * @return An {@link Observable observable} that saves all the given {@link Recipient recipients}
   * locally, emits them and all the {@link Recipient recipients} that are only saved locally.
   */
  @NonNull
  Observable<Set<Recipient>> saveAll(@NonNull Set<Recipient> recipients);

  /**
   * Creates an {@link Observable observable} that emits all the {@link Recipient recipients} saved
   * locally.
   * <p>
   * <em>Note:</em> By default {@link #getAll()} does not operates on a particular {@link
   * rx.Scheduler}.
   *
   * @return An {@link Observable observable} that emits all the {@link Recipient recipients} saved
   * locally.
   */
  @NonNull
  Observable<Set<Recipient>> getAll();
}
