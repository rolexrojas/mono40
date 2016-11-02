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
    // Injects all the dependencies.
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
  public void finish(int additions, int removals) {
    startActivity(MainActivity.getLaunchIntent(this, additions, removals));
    finish();
  }
}
