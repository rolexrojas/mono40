package com.tpago.movil.data.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import javax.inject.Inject;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class NetworkStatusBroadcastReceiver extends BroadcastReceiver {
  @Inject
  NetworkManager networkManager;

  @Override
  public void onReceive(Context context, Intent intent) {
    // TODO
  }
}
