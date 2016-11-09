package com.gbh.movil.ui.splash;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.gbh.movil.App;
import com.gbh.movil.ui.BaseActivity;
import com.gbh.movil.ui.main.MainActivity;

import javax.inject.Inject;

/**
 * {@link SplashScreen Screen} implementation that uses an {@link BaseActivity activity} as
 * container.
 *
 * @author hecvasro
 */
public class SplashActivity extends BaseActivity implements SplashScreen {
  @Inject
  SplashPresenter presenter;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Injects all the annotated dependencies.
    final SplashComponent component = DaggerSplashComponent.builder()
      .appComponent(((App) getApplication()).getComponent())
      .build();
    component.inject(this);
    // Attaches the screen to the presenter.
    presenter.attachScreen(this);
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
  protected void onDestroy() {
    super.onDestroy();
    // Detaches the screen from the presenter.
    presenter.detachScreen();
  }

  @Override
  public void terminate() {
    startActivity(MainActivity.getLaunchIntent(this));
    finish();
  }
}
