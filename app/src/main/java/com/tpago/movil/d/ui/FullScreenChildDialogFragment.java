package com.tpago.movil.d.ui;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.fragment.app.DialogFragment;

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
