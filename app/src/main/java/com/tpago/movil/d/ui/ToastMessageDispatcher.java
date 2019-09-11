package com.tpago.movil.d.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
