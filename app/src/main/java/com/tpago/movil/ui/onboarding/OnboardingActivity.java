package com.tpago.movil.ui.onboarding;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tpago.movil.R;
import com.tpago.movil.ui.BaseActivity;

/**
 * @author hecvasro
 */
public final class OnboardingActivity extends BaseActivity {
  private static final int ID_ROOT = android.R.id.content;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_onboarding);
    getSupportFragmentManager().beginTransaction()
      .replace(ID_ROOT, IndexFragment.newInstance())
      .commit();
  }
}
