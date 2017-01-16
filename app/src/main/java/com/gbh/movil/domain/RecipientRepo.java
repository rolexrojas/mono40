package com.gbh.movil.domain;

import android.support.annotation.NonNull;

import java.util.List;

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
  Observable<List<Recipient>> saveAll(@NonNull List<Recipient> recipients);

  void clear();
}
