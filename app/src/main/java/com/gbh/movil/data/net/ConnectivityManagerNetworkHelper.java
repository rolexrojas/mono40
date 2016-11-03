package com.gbh.movil.data.net;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import com.gbh.movil.Utils;
import com.gbh.movil.domain.NetworkHelper;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class ConnectivityManagerNetworkHelper implements NetworkHelper {
  private final ConnectivityManager connectivityManager;

  private final BehaviorSubject<Boolean> subject = BehaviorSubject.create();

  public ConnectivityManagerNetworkHelper(@NonNull ConnectivityManager connectivityManager) {
    this.connectivityManager = connectivityManager;
    this.checkStatus();
  }

  final void checkStatus() {
    final NetworkInfo info = connectivityManager.getActiveNetworkInfo();
    subject.onNext(Utils.isNotNull(info) && info.isConnected());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final Observable<Boolean> status() {
    return subject.asObservable();
  }
}
