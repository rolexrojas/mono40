package com.tpago.movil.data.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tpago.movil.App;

import javax.inject.Inject;

/**
 * TODO
 *
 * @author hecvasro
 */
public final class NetworkStatusBroadcastReceiver extends BroadcastReceiver {
  @Inject
  NetworkHelper networkHelper;

  @Override
  public void onReceive(Context context, Intent intent) {
    ((App) context).getComponent().inject(this);
    if (networkHelper != null) {
      networkHelper.checkStatus();
    }
  }
}
