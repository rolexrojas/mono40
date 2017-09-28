package com.tpago.movil.dep.init;

import com.tpago.movil.dep.ActivityModule;
import com.tpago.movil.app.ui.ActivityScope;
import com.tpago.movil.dep.init.intro.IntroFragment;
import com.tpago.movil.dep.init.tutorial.TutorialFragment;
import com.tpago.movil.dep.init.register.RegisterComponent;
import com.tpago.movil.dep.init.register.RegisterModule;
import com.tpago.movil.dep.init.signin.SignInFragment;
import com.tpago.movil.dep.init.signin.SignInPresenter;
import com.tpago.movil.dep.init.unlock.UnlockFragment;
import com.tpago.movil.dep.init.unlock.UnlockPresenter;

import dagger.Subcomponent;

/**
 * @author hecvasro
 */
@ActivityScope
@Subcomponent(modules = {
  ActivityModule.class,
  InitModule.class
})
public interface InitComponent {
  RegisterComponent plus(RegisterModule module);

  void inject(InitActivity activity);

  void inject(InitFragment fragment);
  void inject(PhoneNumberInitFragment fragment);
  void inject(PhoneNumberInitPresenter presenter);

  void inject(IntroFragment fragment);

  void inject(TutorialFragment fragment);

  void inject(SignInFragment fragment);
  void inject(SignInPresenter presenter);

  void inject(UnlockFragment fragment);
  void inject(UnlockPresenter presenter);
}
