package com.tpago.movil.ui;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * TODO
 *
 * @author hecvasro
 */
public class ToastMessageDispatcher implements MessageDispatcher {
  private final AppCompatActivity activity;

  /**
   * TODO
   *
   * @param activity
   *   TODO
   */
  public ToastMessageDispatcher(@NonNull AppCompatActivity activity) {
    this.activity = activity;
  }

  @Override
  public void dispatch(@NonNull String message) {
    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
  }
}
