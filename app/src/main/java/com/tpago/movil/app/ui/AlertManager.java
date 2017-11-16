package com.tpago.movil.app.ui;

import android.app.Activity;
import android.support.v7.app.AlertDialog;

import com.tpago.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */
public final class AlertManager {

  public static AlertManager create(Activity activity) {
    return new AlertManager(activity);
  }

  private final Activity activity;

  private AlertManager(Activity activity) {
    this.activity = ObjectHelper.checkNotNull(activity, "activity");
  }

  public final void show(AlertData data) {
    ObjectHelper.checkNotNull(data, "value");

    final AlertDialog alertDialog = new AlertDialog.Builder(this.activity)
      .setTitle(data.title())
      .setMessage(data.message())
      .setPositiveButton(data.positiveButtonText(), data.positiveButtonListener())
      .setNegativeButton(data.negativeButtonText(), data.negativeButtonListener())
      .create();
    alertDialog.show();
  }
}
