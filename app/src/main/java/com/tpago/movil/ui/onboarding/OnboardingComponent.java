package com.tpago.movil.ui.onboarding;

import com.tpago.movil.ui.ActivityScope;
import com.tpago.movil.ui.onboarding.introduction.IntroductionFragment;
import com.tpago.movil.ui.onboarding.registration.RegistrationComponent;
import com.tpago.movil.ui.onboarding.registration.RegistrationModule;

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
