package com.tpago.movil;

import com.tpago.movil.ui.onboarding.OnboardingComponent;
import com.tpago.movil.ui.onboarding.OnboardingModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author hecvasro
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
  OnboardingComponent plus(OnboardingModule module);
}
