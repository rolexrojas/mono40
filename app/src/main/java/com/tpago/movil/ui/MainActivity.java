package com.tpago.movil.ui;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import javax.inject.Inject;

/**
 * TODO
 *
 * @author hecvasro
 */
public class MainActivity extends AppCompatActivity implements MainScreen {
  @Inject
  MainPresenter presenter;

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
  public void showAccountsScreen() {
    // TODO
  }

  @Override
  public void showMessage(@NonNull String message) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
  }

  @Override
  public void showDialog(@NonNull String title, @NonNull String description,
    @NonNull String positiveOptionText, @Nullable OnOptionClickedListener positiveOptionListener,
    @Nullable String neutralOptionText, @Nullable OnOptionClickedListener neutralOptionListener) {
    // TODO
  }

  @Override
  public void showSplashScreen() {
    // TODO
  }

  @Override
  public void hideSplashScreen() {
    // TODO
  }
}
