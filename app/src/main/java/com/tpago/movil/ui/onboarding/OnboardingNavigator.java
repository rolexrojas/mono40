package com.tpago.movil.ui.onboarding;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.tpago.movil.R;
import com.tpago.movil.ui.onboarding.introduction.IntroductionFragment;
import com.tpago.movil.ui.onboarding.registration.PhoneNumberValidationFragment;
import com.tpago.movil.util.Preconditions;

import timber.log.Timber;

/**
 * @author hecvasro
 */
public final class OnboardingNavigator {
  private static final String TAG = "currentFragment";

  private final Context context;
  private final FragmentManager fragmentManager;
  private final int fragmentContainerId;

  OnboardingNavigator(Context context, FragmentManager fragmentManager, int fragmentContainerId) {
    this.context = Preconditions.checkNotNull(context, "context == null");
    this.fragmentManager = Preconditions.checkNotNull(fragmentManager, "fragmentManager == null");
    this.fragmentContainerId = fragmentContainerId;
  }

  private void setFragment(Fragment fragment, boolean animate, boolean addToBackStack) {
    final FragmentTransaction transaction = fragmentManager.beginTransaction();
    if (animate) {
      transaction.setCustomAnimations(
        R.anim.enter_slide_right,
        R.anim.exit_fade,
        R.anim.enter_fade,
        R.anim.exit_slide_right);
    }
    transaction.replace(fragmentContainerId, fragment, TAG);
    if (addToBackStack) {
      transaction.addToBackStack(null);
    }
    transaction.commit();
  }

  final void startIntroduction() {
    Timber.d("Starting the introduction process");
    setFragment(IntroductionFragment.create(), true, false);
  }

  public final void startInitialization() {
    Timber.d("Starting the initialization process");
    setFragment(InitializationFragment.create(), false, false);
  }

  public final void startRegistration() {
    Timber.d("Starting the registration process");
    setFragment(PhoneNumberValidationFragment.create(), true, true);
  }
}
