package com.tpago.movil.d.data.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.annotation.NonNull;

import com.tpago.movil.util.ObjectHelper;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author hecvasro
 */
@Deprecated
@Singleton
final class ConnectivityManagerNetworkHelper implements NetworkHelper {

  private final ConnectivityManager connectivityManager;

  @Inject
  ConnectivityManagerNetworkHelper(@NonNull Context context) {
    connectivityManager = (ConnectivityManager) context.getSystemService(
      Context.CONNECTIVITY_SERVICE
    );
  }

  @Override
  public boolean isNetworkAvailable() {
    final NetworkInfo info = connectivityManager.getActiveNetworkInfo();
    return ObjectHelper.isNotNull(info) && info.isConnectedOrConnecting();
  }
}
