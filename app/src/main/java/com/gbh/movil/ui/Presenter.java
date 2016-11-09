package com.gbh.movil.ui;

import android.support.annotation.NonNull;

import com.gbh.movil.Utils;

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
  protected final boolean isScreenAttached() {
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
  public void start() {
    assertScreen();
  }

  /**
   * TODO
   */
  public void stop() {
    assertScreen();
  }

  /**
   * TODO
   */
  public final void detachScreen() {
    assertScreen();
    this.screen = null;
  }
}
