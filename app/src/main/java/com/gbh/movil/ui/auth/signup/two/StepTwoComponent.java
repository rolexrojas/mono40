package com.gbh.movil.ui.auth.signup.two;

import com.gbh.movil.ui.FragmentScope;
import com.gbh.movil.ui.auth.signup.SignUpComponent;

import dagger.Component;

/**
 * @author hecvasro
 */
@FragmentScope
@Component(dependencies = SignUpComponent.class, modules = StepTwoModule.class)
interface StepTwoComponent {
  void inject(StepTwoFragment fragment);
}
