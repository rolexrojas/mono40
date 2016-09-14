package com.tpago.movil.ui;

import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

/**
 * TODO
 *
 * @author hecvasro
 */
public class MainActivity extends AppCompatActivity implements MainScreen {
  /**
   * TODO
   */
  @Inject
  MainPresenter presenter;

  @Override
  public void showSplashScreen() {
    // TODO
  }

  @Override
  public void hideSplashScreen() {
    // TODO
  }
}
