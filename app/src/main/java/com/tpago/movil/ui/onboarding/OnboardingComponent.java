package com.tpago.movil.ui.onboarding;

import com.tpago.movil.AppComponent;
import com.tpago.movil.UserStore;
import com.tpago.movil.ui.ActivityScope;
import com.tpago.movil.ui.onboarding.introduction.IntroductionFragment;

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
  void inject(OnboardingActivity activity);

  void inject(InitializationFragment fragment);
  void inject(IntroductionFragment fragment);

  UserStore provideUserStore();

  OnboardingNavigator provideNavigator();
}
