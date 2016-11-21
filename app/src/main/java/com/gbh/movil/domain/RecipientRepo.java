package com.gbh.movil.domain;

import android.support.annotation.NonNull;

import java.util.Set;

import rx.Observable;

/**
 * TODO
 *
 * @author hecvasro
 */
public interface RecipientRepo extends RecipientProvider {
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
