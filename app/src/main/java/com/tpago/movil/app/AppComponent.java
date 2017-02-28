package com.tpago.movil.app;

import com.tpago.movil.api.ApiModule;
import com.tpago.movil.init.InitComponent;
import com.tpago.movil.init.InitModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author hecvasro
 */
@Singleton
@Component(modules = {
  AppModule.class,
  ApiModule.class
})
public interface AppComponent {
  InitComponent plus(InitModule module);

  void inject(AvatarCreationDialogFragment fragment);
}
