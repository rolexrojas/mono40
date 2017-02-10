package com.tpago.movil.d.ui.auth.signin;

import com.tpago.movil.d.AppComponent;
import com.tpago.movil.d.ui.ActivityModule;
import com.tpago.movil.d.ui.ActivityScope;

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
