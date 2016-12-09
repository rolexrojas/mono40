package com.gbh.movil.ui.auth.signin;

import com.gbh.movil.AppComponent;
import com.gbh.movil.ui.ActivityModule;
import com.gbh.movil.ui.ActivityScope;

import dagger.Component;

/**
 * TODO
 *
 * @author hecvasro
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = { ActivityModule.class,
  SignInModule.class })
interface SignInComponent {
  /**
   * TODO
   *
   * @param activity
   *   TODO
   */
  void inject(SignInActivity activity);
}
