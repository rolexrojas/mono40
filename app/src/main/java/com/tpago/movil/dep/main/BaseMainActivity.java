package com.tpago.movil.dep.main;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tpago.movil.dep.TimeOutManager;
import com.tpago.movil.dep.BaseActivity;
import com.tpago.movil.dep.init.InitActivity;

import javax.inject.Inject;

/**
 * @author hecvasro
 */
public abstract class BaseMainActivity
  extends BaseActivity
  implements TimeOutManager.TimeOutHandler {
  @Inject TimeOutManager timeOutManager;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Starts the time out manager.
    timeOutManager.start();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    // Stops the time out manager.
    timeOutManager.stop();
  }

  @Override
  public void onUserInteraction() {
    super.onUserInteraction();
    // Resets the time out manager.
    timeOutManager.reset();
  }

  @Override
  public void handleTimeOut() {
    startActivity(InitActivity.getLaunchIntent(this));
    finish();
  }
}
