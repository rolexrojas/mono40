package com.tpago.movil.ui.onboarding.registration;

import com.tpago.movil.ui.FragmentScope;

import dagger.Module;
import dagger.Provides;

/**
 * @author hecvasro
 */
@Module
public class RegistrationModule {
  @Provides
  @FragmentScope
  RegistrationData provideRegistrationDataBuilder() {
    return new RegistrationData();
  }
}
