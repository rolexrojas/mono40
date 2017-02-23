package com.tpago.movil.onboarding.registration;

import com.tpago.movil.app.FragmentScope;

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
