package com.tpago.movil.d.ui;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.fragment.app.DialogFragment;

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
