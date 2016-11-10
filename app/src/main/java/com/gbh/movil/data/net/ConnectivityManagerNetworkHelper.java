package com.gbh.movil.data.net;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import com.gbh.movil.Utils;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class ConnectivityManagerNetworkHelper implements NetworkHelper {
  private final ConnectivityManager connectivityManager;

  public ConnectivityManagerNetworkHelper(@NonNull ConnectivityManager connectivityManager) {
    this.connectivityManager = connectivityManager;
  }

  @Override
  public boolean isNetworkAvailable() {
    final NetworkInfo info = connectivityManager.getActiveNetworkInfo();
    return Utils.isNotNull(info) && info.isConnectedOrConnecting();
  }
}
