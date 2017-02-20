package com.tpago.movil.ui.onboarding;

import com.tpago.movil.AppComponent;
import com.tpago.movil.UserStore;
import com.tpago.movil.ui.ActivityScope;

import dagger.Component;

/**
 * @author hecvasro
 */
@ActivityScope
@Component(
  dependencies = AppComponent.class,
  modules = OnboardingModule.class
)
public interface OnboardingComponent {
  UserStore provideUserStore();
}
