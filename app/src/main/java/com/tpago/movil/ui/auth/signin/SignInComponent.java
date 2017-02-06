package com.tpago.movil.ui.auth.signin;

import com.tpago.movil.AppComponent;
import com.tpago.movil.ui.ActivityModule;
import com.tpago.movil.ui.ActivityScope;

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
