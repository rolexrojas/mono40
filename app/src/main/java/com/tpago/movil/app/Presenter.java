package com.tpago.movil.app;

import com.tpago.movil.util.Preconditions;

/**
 * @author hecvasro
 */
public abstract class Presenter<V extends Presenter.View> {
  protected final V view;

  public Presenter(V view) {
    this.view = Preconditions.assertNotNull(view, "view == null");
  }

  public void onViewStarted() {
  }

  public void onViewStopped() {
  }

  public interface View {
  }
}
