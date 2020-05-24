package com.mono40.movil.app.ui.loader.takeover;

import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.mono40.movil.R;
import com.mono40.movil.app.ui.fragment.base.BaseDialogFragment;

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
