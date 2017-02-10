package com.tpago.movil.d.ui.auth.signup.one;

import com.tpago.movil.d.ui.FragmentScope;
import com.tpago.movil.d.ui.auth.signup.SignUpComponent;

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
