package com.gbh.movil.domain;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Set;

import rx.Observable;

/**
 * TODO
 *
 * @author hecvasro
 */
public interface RecipientProvider {
  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  Observable<Set<Recipient>> getAll(@Nullable String query);
}
