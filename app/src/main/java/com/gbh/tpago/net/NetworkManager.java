package com.gbh.tpago.net;

import android.net.ConnectivityManager;
import android.support.annotation.NonNull;

import rx.Observable;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class NetworkManager {
  private final ConnectivityManager connectivityManager;

  /**
   * TODO
   *
   * @param connectivityManager TODO
   */
  public NetworkManager(@NonNull ConnectivityManager connectivityManager) {
    this.connectivityManager = connectivityManager;
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  public final Observable<Boolean> status() {
    // TODO
    return Observable.error(new UnsupportedOperationException());
  }
}
