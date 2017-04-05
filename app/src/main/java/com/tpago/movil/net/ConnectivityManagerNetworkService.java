package com.tpago.movil.net;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import static com.tpago.movil.util.Objects.checkIfNotNull;
import static com.tpago.movil.util.Preconditions.assertNotNull;

/**
 * @author hecvasro
 */
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
