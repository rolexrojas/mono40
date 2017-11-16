package com.tpago.movil.app.ui.main;

import android.support.annotation.StringRes;

import com.tpago.movil.app.ui.BaseFragment;
import com.tpago.movil.d.ui.main.DepMainActivity;

/**
 * @author hecvasro
 */
@Deprecated
public abstract class BaseMainFragment extends BaseFragment {

  @StringRes
  protected abstract int titleResId();

  @Override
  public void onStart() {
    super.onStart();

    // Updates the title.
    DepMainActivity.get(this.getActivity())
      .toolbar()
      .setTitle(this.titleResId());
  }
}
