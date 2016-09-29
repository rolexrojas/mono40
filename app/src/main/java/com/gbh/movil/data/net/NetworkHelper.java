package com.gbh.movil.data.net;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class NetworkHelper {
  private final ConnectivityManager connectivityManager;

  /**
   * TODO
   */
  private final BehaviorSubject<Boolean> subject = BehaviorSubject.create();

  /**
   * TODO
   *
   * @param connectivityManager
   *   TODO
   */
  public NetworkHelper(@NonNull ConnectivityManager connectivityManager) {
    this.connectivityManager = connectivityManager;
    this.checkStatus();
  }

  /**
   * TODO
   */
  final void checkStatus() {
    final NetworkInfo info = connectivityManager.getActiveNetworkInfo();
    subject.onNext(info != null && info.isConnected());
  }

  /**
   * TODO
   *
   * @return TODO
   */
  @NonNull
  public final Observable<Boolean> status() {
    return subject.asObservable();
  }
}
