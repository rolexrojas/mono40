package com.gbh.movil.ui.index;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.gbh.movil.App;
import com.gbh.movil.ui.ActivityModule;
import com.gbh.movil.ui.BaseActivity;
import com.gbh.movil.ui.auth.AuthIndexActivity;
import com.gbh.movil.ui.auth.SignInActivity;

import javax.inject.Inject;

/**
 * {@link IndexScreen Screen} implementation that uses an {@link BaseActivity activity} as
 * container.
 *
 * @author hecvasro
 */
public class IndexActivity extends BaseActivity implements IndexScreen {
  @Inject
  IndexPresenter presenter;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Injects all the annotated dependencies.
    final IndexComponent component = DaggerIndexComponent.builder()
      .appComponent(((App) getApplication()).getComponent())
      .activityModule(new ActivityModule(this))
      .build();
    component.inject(this);
    // Attaches the screen to the presenter.
    presenter.attachScreen(this);
    // Creates the presenter.
    presenter.create();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    // Destroys the presenter.
    presenter.destroy();
    // Detaches the screen from the presenter.
    presenter.detachScreen();
  }

  @Override
  public void startSignInScreen() {
    startActivity(SignInActivity.getLaunchIntent(this));
  }

  @Override
  public void startAuthIndexScreen() {
    startActivity(AuthIndexActivity.getLaunchIntent(this));
  }
}
