package com.mono40.movil.dep;

import com.mono40.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */
@Deprecated
public abstract class Presenter<V extends Presenter.View> {

  protected final V view;

  public Presenter(V view) {
    this.view = ObjectHelper.checkNotNull(view, "view");
  }

  public void onViewStarted() {
  }

  public void onViewStopped() {
  }

  public interface View {
  }
}
