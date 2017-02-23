package com.tpago.movil.ui.onboarding;

import com.tpago.movil.R;
import com.tpago.movil.ui.ActivityScope;
import com.tpago.movil.util.Preconditions;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public final class OnboardingModule {
  private final OnboardingActivity activity;

  OnboardingModule(OnboardingActivity activity) {
    this.activity = Preconditions.checkNotNull(activity, "activity == null");
  }

  @Provides
  @ActivityScope
  LogoAnimator provideLogoAnimator() {
    return new LogoAnimator(
      activity.logo,
      activity.placeholderView,
      activity.getResources().getInteger(R.integer.anim_duration_test));
  }
}
