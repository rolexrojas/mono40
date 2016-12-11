package com.gbh.movil.ui.auth.signup.one;

import com.gbh.movil.ui.FragmentScope;
import com.gbh.movil.ui.auth.signup.SignUpComponent;

import dagger.Component;

/**
 * TODO
 *
 * @author hecvasro
 */
@FragmentScope
@Component(dependencies = SignUpComponent.class, modules = StepOneModule.class)
interface StepOneComponent {
  /**
   * TODO
   *
   * @param fragment
   *   TODO
   */
  void inject(StepOneFragment fragment);
}
