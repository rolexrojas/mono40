package com.mono40.movil.app.ui.main;

import androidx.annotation.StringRes;

import com.mono40.movil.app.ui.fragment.base.FragmentBase;
import com.mono40.movil.d.ui.main.DepMainActivityBase;

/**
 * @author hecvasro
 */
public abstract class BaseMainFragment extends FragmentBase {

  @StringRes
  protected abstract int titleResId();

  protected abstract String subTitle();


  @Override
  public void onStart() {
    super.onStart();

    // Updates the title.
    DepMainActivityBase.get(this.getActivity())
            .toolbar()
            .setTitle(this.titleResId());
  }

  @Override
  public void onResume() {
    super.onResume();

    // Updates the Subtitle.
    DepMainActivityBase mainActivityBase = DepMainActivityBase.get(this.getActivity());
    if (mainActivityBase != null) {
      mainActivityBase.toolbar()
              .setSubtitle(this.subTitle());
    }
  }

  @Override
  public void onPause() {
    super.onPause();

    // Updates the Subtitle.
    DepMainActivityBase.get(this.getActivity())
            .toolbar()
            .setSubtitle("");
  }
}
