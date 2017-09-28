package com.tpago.movil.dep;

/**
 * @author hecvasro
 */
@Deprecated
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
