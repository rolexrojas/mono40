package com.tpago.movil.dep.net;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import static com.tpago.movil.dep.Objects.checkIfNotNull;
import static com.tpago.movil.dep.Preconditions.assertNotNull;

/**
 * @author hecvasro
 */
@Deprecated
final class ConnectivityManagerNetworkService implements NetworkService {
  private final ConnectivityManager connectivityManager;

  ConnectivityManagerNetworkService(ConnectivityManager connectivityManager) {
    this.connectivityManager = assertNotNull(connectivityManager, "connectivityManager == null");
  }

  @Override
  public boolean checkIfAvailable() {
    final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
    return checkIfNotNull(networkInfo) && networkInfo.isConnectedOrConnecting();
  }
}
