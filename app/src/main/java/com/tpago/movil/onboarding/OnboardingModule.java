package com.tpago.movil.onboarding;

import com.tpago.movil.R;
import com.tpago.movil.app.ActivityModule;
import com.tpago.movil.app.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public final class OnboardingModule extends ActivityModule<OnboardingActivity> {
  OnboardingModule(OnboardingActivity activity) {
    super(activity);
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
