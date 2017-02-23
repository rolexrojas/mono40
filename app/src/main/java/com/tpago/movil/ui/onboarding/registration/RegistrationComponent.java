package com.tpago.movil.ui.onboarding.registration;

import com.tpago.movil.ui.FragmentScope;
import com.tpago.movil.ui.onboarding.OnboardingActivity;
import com.tpago.movil.ui.onboarding.OnboardingComponent;

import dagger.Component;
import dagger.Subcomponent;

/**
 * @author hecvasro
 */
@FragmentScope
@Subcomponent(modules = RegistrationModule.class)
public interface RegistrationComponent {
  void inject(PhoneNumberValidationFragment fragment);
}
