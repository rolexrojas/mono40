package com.tpago.movil.app.ui.init.unlock;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.tpago.movil.R;
import com.tpago.movil.app.ui.BaseFragment;
import com.tpago.movil.app.ui.FragmentReplacer;
import com.tpago.movil.dep.init.InitActivity;
import com.tpago.movil.session.SessionManager;
import com.tpago.movil.session.UnlockMethod;

import javax.inject.Inject;

/**
 * @author hecvasro
 */
public final class UnlockFragment extends BaseFragment {

  public static UnlockFragment create() {
    return new UnlockFragment();
  }

  private FragmentReplacer fragmentReplacer;

  @Inject SessionManager sessionManager;

  @Override
  protected int layoutResId() {
    return R.layout.unlock;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    this.fragmentReplacer = FragmentReplacer
      .create(this.getChildFragmentManager(), R.id.subcontainerFrameLayout);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    InitActivity.get(this.getActivity())
      .getInitComponent()
      .inject(this);

    final Fragment fragment;
    if (this.sessionManager.isUnlockMethodEnabled(UnlockMethod.CODE)) {
      fragment = CodeUnlockFragment.create();
    } else if (this.sessionManager.isUnlockMethodEnabled(UnlockMethod.FINGERPRINT)) {
      fragment = FingerprintUnlockFragment.create();
    } else {
      fragment = PasswordUnlockFragment.create();
    }

    this.fragmentReplacer.begin(fragment)
      .commit();
  }
}
