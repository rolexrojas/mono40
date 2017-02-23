package com.tpago.movil.app;

import com.tpago.movil.api.ApiModule;
import com.tpago.movil.onboarding.OnboardingComponent;
import com.tpago.movil.onboarding.OnboardingModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author hecvasro
 */
@Singleton
@Component(modules = {
  AppModule.class,
  ApiModule.class
})
public interface AppComponent {
  OnboardingComponent plus(OnboardingModule module);
}
