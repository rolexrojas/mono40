package com.tpago.movil.d.domain;

import android.support.annotation.NonNull;

import java.util.List;

import rx.Observable;

/**
 * TODO
 *
 * @author hecvasro
 */
@Deprecated
public interface TransactionProvider {
  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  Observable<List<Transaction>> getAll();
}
