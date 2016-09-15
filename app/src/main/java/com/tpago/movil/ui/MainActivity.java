package com.tpago.movil.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.tpago.movil.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * TODO
 *
 * @author hecvasro
 */
public class MainActivity extends AppCompatActivity implements MainScreen {
  private Unbinder unbinder;

  @Inject
  MainPresenter presenter;

  @BindView(R.id.toolbar)
  Toolbar toolbar;

  @BindView(R.id.frame_layout_container)
  FrameLayout containerFrameLayout;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Sets the content layout identifier.
    setContentView(R.layout.activity_main);
    // Binds all the annotated views and methods.
    unbinder = ButterKnife.bind(this);
    // Prepares the action bar.
    setSupportActionBar(toolbar);
    final ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayShowTitleEnabled(true);
    }
  }

  @Override
  protected void onStart() {
    super.onStart();
//    presenter.start();
  }

  @Override
  protected void onStop() {
    super.onStop();
//    presenter.stop();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    // Unbinds all the annotated views and methods.
    unbinder.unbind();
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
