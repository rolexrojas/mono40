package com.mono40.movil.app.ui.init.unlock;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.View;

import com.mono40.movil.R;
import com.mono40.movil.app.ui.fragment.base.FragmentBase;
import com.mono40.movil.app.ui.fragment.FragmentReplacer;
import com.mono40.movil.dep.init.InitActivityBase;
import com.mono40.movil.session.SessionManager;
import com.mono40.movil.session.UnlockMethod;

import javax.inject.Inject;

/**
 * @author hecvasro
 */
public final class UnlockFragment extends FragmentBase {

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

    InitActivityBase.get(this.getActivity())
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
