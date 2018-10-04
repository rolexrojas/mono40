package com.tpago.movil.app.ui.loader.takeover;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import com.tpago.movil.R;
import com.tpago.movil.app.ui.fragment.base.BaseDialogFragment;

/**
 * @author Hector Vasquez
 */
public final class TakeoverLoaderDialogFragment extends BaseDialogFragment {

  public static TakeoverLoaderDialogFragment create() {
    return new TakeoverLoaderDialogFragment();
  }

  @LayoutRes
  @Override
  protected int layoutResId() {
    return R.layout.takeover_loader;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    this.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.TakeoverLoaderTheme);
  }
}
