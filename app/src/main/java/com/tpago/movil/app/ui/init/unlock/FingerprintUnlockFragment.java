package com.tpago.movil.app.ui.init.unlock;

import android.os.Bundle;
import android.view.View;

import com.tpago.movil.R;
import com.tpago.movil.dep.init.InitActivity;

/**
 * @author hecvasro
 */
public final class FingerprintUnlockFragment extends BaseUnlockFragment {

  static FingerprintUnlockFragment create() {
    return new FingerprintUnlockFragment();
  }

  @Override
  protected int layoutResId() {
    return R.layout.unlock_fingerprint;
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    InitActivity.get(this.getActivity())
      .getInitComponent()
      .inject(this);
  }

  @Override
  public void onResume() {
    super.onResume();

    this.logoAnimator.moveTopAndScaleDown();
  }
}
