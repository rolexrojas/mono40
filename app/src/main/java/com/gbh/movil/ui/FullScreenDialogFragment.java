package com.gbh.movil.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.app.DialogFragment;

/**
 * TODO
 * <p>
 * Instead use {@link FullScreenChildDialogFragment}
 *
 * @author hecvasro
 */
@Deprecated
public abstract class FullScreenDialogFragment extends DialogFragment {
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
