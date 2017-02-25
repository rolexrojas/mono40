package com.tpago.movil.init;

import com.tpago.movil.app.ActivityScope;
import com.tpago.movil.init.intro.IntroFragment;
import com.tpago.movil.init.register.RegisterComponent;
import com.tpago.movil.init.register.RegisterModule;

import dagger.Subcomponent;

/**
 * @author hecvasro
 */
@ActivityScope
@Subcomponent(modules = InitModule.class)
public interface InitComponent {
  RegisterComponent plus(RegisterModule module);

  void inject(InitActivity activity);

  void inject(InitFragment fragment);
  void inject(IntroFragment fragment);
}
