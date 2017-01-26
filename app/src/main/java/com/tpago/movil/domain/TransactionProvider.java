package com.tpago.movil.domain;

import android.support.annotation.NonNull;

import java.util.List;

import rx.Observable;

/**
 * TODO
 *
 * @author hecvasro
 */
public interface TransactionProvider {
  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  Observable<List<Transaction>> getAll();
}
