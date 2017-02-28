package com.tpago.movil.dep.ui.auth.signup.two;

import com.tpago.movil.dep.ui.FragmentScope;
import com.tpago.movil.dep.ui.auth.signup.SignUpComponent;

import dagger.Component;

/**
 * @author hecvasro
 */
@FragmentScope
@Component(dependencies = SignUpComponent.class, modules = StepTwoModule.class)
interface StepTwoComponent {
  void inject(StepTwoFragment fragment);
}
