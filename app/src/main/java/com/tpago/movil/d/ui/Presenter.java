package com.tpago.movil.d.ui;

import androidx.annotation.NonNull;

import com.tpago.movil.util.ObjectHelper;

/**
 * @author hecvasro
 */
@Deprecated
public abstract class Presenter<S extends Screen> {

  @Deprecated
  protected S screen;

  @Deprecated
  private boolean isScreenAttached() {
    return ObjectHelper.isNotNull(screen);
  }

  @Deprecated
  protected final void assertScreen() {
    if (!isScreenAttached()) {
      throw new NotAttachedException("Screen must be attached");
    }
  }

  @Deprecated
  public final void attachScreen(@NonNull S screen) {
    if (this.isScreenAttached()) {
      this.detachScreen();
    }
    this.screen = screen;
  }

  @Deprecated
  public final void detachScreen() {
    assertScreen();
    this.screen = null;
  }
}
