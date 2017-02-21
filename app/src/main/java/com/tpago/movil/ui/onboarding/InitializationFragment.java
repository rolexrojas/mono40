package com.tpago.movil.ui.onboarding;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tpago.movil.UserStore;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * @author hecvasro
 */
public final class InitializationFragment extends OnboardingFragment {
  @Inject
  UserStore userStore;

  @Inject
  LogoAnimator logoAnimator;
  @Inject
  OnboardingNavigator onboardingNavigator;

  public static InitializationFragment create() {
    return new InitializationFragment();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Injects all annotated dependencies.
    getParentComponent().inject(this);
  }

  @Override
  public void onResume() {
    super.onResume();
    logoAnimator.reset();
    Timber.d("Starting the initialization process");
    if (userStore.isSet()) {
      // TODO: Start the authentication process.
      Timber.d("Starting the authentication process");
    } else {
      // TODO: Request permissions
      logoAnimator.moveAndScale();
      onboardingNavigator.startIntroduction();
    }
  }
}
