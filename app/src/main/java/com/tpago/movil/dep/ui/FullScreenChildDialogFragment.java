package com.tpago.movil.dep.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.app.DialogFragment;

/**
 * @author hecvasro
 */
public abstract class FullScreenChildDialogFragment<T extends Container<?>>
  extends ChildDialogFragment<T> {
  /**
   * TODO
   *
   * @return TODO
   */
  @StyleRes
  protected abstract int getCustomTheme();

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setStyle(DialogFragment.STYLE_NO_FRAME, getCustomTheme());
  }
}
