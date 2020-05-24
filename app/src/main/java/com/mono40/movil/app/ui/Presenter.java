package com.mono40.movil.app.ui;

import com.mono40.movil.util.ObjectHelper;

/**
 * Represents the P of the MVP pattern.
 *
 * @author hecvasro
 */
public abstract class Presenter<T extends Presentation> {

  protected final T presentation;

  protected Presenter(T presentation) {
    this.presentation = ObjectHelper.checkNotNull(presentation, "presentation");
  }

  /**
   * Called when the {@link Presentation presentation} is being resumed.
   */
  public void onPresentationResumed() {
  }

  /**
   * Called when the {@link Presentation presentation} is being paused.
   */
  public void onPresentationPaused() {
  }
}
