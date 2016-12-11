package com.gbh.movil.ui.auth.signup;

import com.gbh.movil.AppComponent;
import com.gbh.movil.ui.ActivityComponent;
import com.gbh.movil.ui.ActivityModule;
import com.gbh.movil.ui.ActivityScope;

import dagger.Component;

/**
 * TODO
 *
 * @author hecvasro
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
interface SignUpComponent extends ActivityComponent {
  /**
   * TODO
   *
   * @param activity
   *   TODO
   */
  void inject(SignUpActivity activity);
}
