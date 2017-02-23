package com.tpago.movil.onboarding;

import com.tpago.movil.app.ActivityScope;
import com.tpago.movil.onboarding.introduction.IntroductionFragment;
import com.tpago.movil.onboarding.registration.RegistrationComponent;
import com.tpago.movil.onboarding.registration.RegistrationModule;

import dagger.Subcomponent;

/**
 * @author hecvasro
 */
@ActivityScope
@Subcomponent(modules = OnboardingModule.class)
public interface OnboardingComponent {
  RegistrationComponent plus(RegistrationModule module);

  void inject(OnboardingActivity activity);

  void inject(InitializationFragment fragment);
  void inject(IntroductionFragment fragment);
}
