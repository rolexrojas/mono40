package com.tpago.movil.app.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import com.tpago.movil.R;

/**
 * @author Hector Vasquez
 */
public final class TakeoverLoaderDialogFragment extends BaseDialogFragment {

  public static TakeoverLoaderDialogFragment create() {
    return new TakeoverLoaderDialogFragment();
  }

  @Override
  protected int layoutResId() {
    return R.layout.fragment_loader_takeover;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Sets the style of the dialog fragment.
    this.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.TakeoverLoaderTheme);
  }
}
