package com.tpago.movil.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * @author hecvasro
 */
public abstract class BaseFragment<P extends Presenter<?>> extends Fragment {
  private P presenter;

  protected final P getPresenter() {
    return presenter;
  }

  protected abstract P onCreatePresenter();

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Creates the presenter.
    presenter = onCreatePresenter();
  }

  @Override
  public void onStart() {
    super.onStart();
    // Indicates the presenter that the view has started.
    presenter.onViewStarted();
  }

  @Override
  public void onStop() {
    super.onStop();
    // Indicates the presenter that the view has stopped.
    presenter.onViewStopped();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    // Destroys the presenter.
    presenter = null;
  }
}
