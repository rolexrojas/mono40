package com.mono40.movil.dep.net;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.mono40.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */
@Deprecated
final class ConnectivityManagerNetworkService implements NetworkService {

  private final ConnectivityManager connectivityManager;

  ConnectivityManagerNetworkService(ConnectivityManager connectivityManager) {
    this.connectivityManager = ObjectHelper
      .checkNotNull(connectivityManager, "connectivityManager");
  }

  @Override
  public boolean checkIfAvailable() {
    final NetworkInfo networkInfo = this.connectivityManager.getActiveNetworkInfo();
    if (ObjectHelper.isNull(networkInfo)) {
      return false;
    } else {
      return networkInfo.isConnectedOrConnecting();
    }
  }
}
