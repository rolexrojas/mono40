package com.tpago.movil.onboarding.registration;

import com.tpago.movil.app.FragmentScope;

import dagger.Subcomponent;

/**
 * @author hecvasro
 */
@FragmentScope
@Subcomponent(modules = RegistrationModule.class)
public interface RegistrationComponent {
  void inject(PhoneNumberFormFragment fragment);
  void inject(NameFormFragment fragment);
}
