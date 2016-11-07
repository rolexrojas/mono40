package com.gbh.movil.data.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.gbh.movil.App;
import com.gbh.movil.Utils;
import com.gbh.movil.domain.NetworkHelper;

import javax.inject.Inject;

/**
 * {@link BroadcastReceiver} implementation that gets notified every time the network status
 * changes.
 *
 * @author hecvasro
 */
public final class NetworkStatusBroadcastReceiver extends BroadcastReceiver {
  @Inject
  NetworkHelper networkHelper;

  @Override
  public void onReceive(Context context, Intent intent) {
    ((App) context).getComponent().inject(this);
    if (Utils.isNotNull(networkHelper)) {
      ((ConnectivityManagerNetworkHelper) networkHelper).checkStatus(); // TODO: Find a way to get rid of this cast.
    }
  }
}
