package com.tpago.movil.ui;

import android.content.Context;
import android.support.v7.app.AlertDialog;

import com.tpago.movil.R;

/**
 * @author hecvasro
 */
public final class DialogCreator {
  private DialogCreator() {
  }

  public static AlertDialog featureNotAvailable(Context context) {
    return new AlertDialog.Builder(context)
      .setTitle(R.string.dialog_title_feature_not_available)
      .setMessage(R.string.dialog_message_feature_not_available)
      .setPositiveButton(R.string.dialog_positive_text_feature_not_available, null)
      .setCancelable(false)
      .create();
  }
}
