package com.tpago.movil.ui;

import android.support.annotation.NonNull;

import com.tpago.movil.misc.Utils;

/**
 * TODO
 *
 * @author hecvasro
 */
public abstract class Presenter<S extends Screen> {
  protected S screen;

  /**
   * TODO
   *
   * @return TODO
   */
  private boolean isScreenAttached() {
    return Utils.isNotNull(screen);
  }

  /**
   * TODO
   */
  protected final void assertScreen() {
    if (!isScreenAttached()) {
      throw new NotAttachedException("Screen must be attached");
    }
  }

  /**
   * TODO
   *
   * @param screen
   *   TODO
   */
  public final void attachScreen(@NonNull S screen) {
    if (this.isScreenAttached()) {
      this.detachScreen();
    }
    this.screen = screen;
  }

  /**
   * TODO
   */
  public final void detachScreen() {
    assertScreen();
    this.screen = null;
  }
}
