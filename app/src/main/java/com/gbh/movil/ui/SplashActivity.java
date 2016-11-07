package com.gbh.movil.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.gbh.movil.App;
import com.gbh.movil.ui.main.MainActivity;

import javax.inject.Inject;

/**
 * TODO
 *
 * @author hecvasro
 */
public class SplashActivity extends BaseActivity implements SplashScreen {
  @Inject
  SplashPresenter presenter;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    final SplashComponent component = DaggerSplashComponent.builder()
      .appComponent(((App) getApplication()).getComponent())
      .splashModule(new SplashModule(this))
      .build();
    component.inject(this);
  }

  @Override
  protected void onStart() {
    super.onStart();
    presenter.start();
  }

  @Override
  protected void onStop() {
    super.onStop();
    presenter.stop();
  }

  @Override
  public void finish(boolean wereAccountAdditions, boolean wereAccountRemovals) {
    startActivity(MainActivity.getLaunchIntent(this, wereAccountAdditions, wereAccountRemovals));
    finish();
  }
}
