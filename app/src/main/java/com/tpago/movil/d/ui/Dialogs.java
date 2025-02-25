package com.tpago.movil.d.ui;

import android.content.Context;
import androidx.appcompat.app.AlertDialog;

import com.tpago.movil.R;

/**
 * @author hecvasro
 */
public final class Dialogs {
  public static AlertDialog.Builder builder(Context context) {
    return new AlertDialog.Builder(context);
  }

  private Dialogs() {
  }

  public static AlertDialog featureNotAvailable(Context context) {
    return builder(context)
      .setTitle(R.string.dialog_title_feature_not_available)
      .setMessage(R.string.dialog_message_feature_not_available)
      .setPositiveButton(R.string.dialog_positive_text_feature_not_available, null)
      .setCancelable(false)
      .create();
  }
}
