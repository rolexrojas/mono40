package com.gbh.movil.ui.splash;

import com.gbh.movil.AppComponent;
import com.gbh.movil.ui.ActivityScope;

import dagger.Component;

/**
 * @author hecvasro
 */
@ActivityScope
@Component(modules = SplashModule.class, dependencies = AppComponent.class)
interface SplashComponent {
  void inject(SplashActivity activity);
}
