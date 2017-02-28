package com.tpago.movil.dep.ui.auth.signin;

import com.tpago.movil.app.AppComponent;
import com.tpago.movil.dep.ui.ActivityModule;
import com.tpago.movil.dep.ui.ActivityScope;

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
