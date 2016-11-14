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

  /**
   * TODO
   *
   * @param recipient
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  Observable<Recipient> save(@NonNull Recipient recipient);

  /**
   * TODO
   *
   * @param recipients
   *   TODO
   *
   * @return TODO
   */
  @NonNull
  Observable<Set<Recipient>> saveAll(@NonNull Set<Recipient> recipients);
}
