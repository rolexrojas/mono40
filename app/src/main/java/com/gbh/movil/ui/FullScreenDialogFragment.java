package com.gbh.movil.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import com.gbh.movil.R;

/**
 * TODO
 * @author hecvasro
 */
public abstract class FullScreenDialogFragment extends DialogFragment {
  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setStyle(DialogFragment.STYLE_NO_FRAME, R.style.FullScreenDialogTheme);
  }
}
