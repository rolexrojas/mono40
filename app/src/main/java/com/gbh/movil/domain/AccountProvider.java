package com.gbh.movil.domain;

import android.support.annotation.NonNull;

import java.util.Set;

import rx.Observable;

/**
 * TODO
 *
 * @author hecvasro
 */
public interface AccountProvider {
  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  Observable<Set<Account>> getAll();
}
