package com.gbh.movil.domain;

import android.support.annotation.NonNull;

import rx.Observable;

/**
 * TODO
 *
 * @author hecvasro
 */
public interface NetworkHelper {
  /**
   * Creates an {@link Observable observable} that emits the current network status.
   *
   * @return An {@link Observable observable} that emits the current network status. True if it is
   * available, false otherwise.
   */
  @NonNull
  Observable<Boolean> status();
}
