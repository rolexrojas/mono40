package com.gbh.movil.data.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import com.gbh.movil.misc.Utils;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * TODO
 *
 * @author hecvasro
 */
@Singleton
final class ConnectivityManagerNetworkHelper implements NetworkHelper {
  private final ConnectivityManager connectivityManager;

  @Inject
  ConnectivityManagerNetworkHelper(@NonNull Context context) {
    connectivityManager = (ConnectivityManager) context.getSystemService(
      Context.CONNECTIVITY_SERVICE);
  }

  @Override
  public boolean isNetworkAvailable() {
    final NetworkInfo info = connectivityManager.getActiveNetworkInfo();
    return Utils.isNotNull(info) && info.isConnectedOrConnecting();
  }
}
