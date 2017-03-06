package com.tpago.movil.init;

import com.tpago.movil.app.ActivityScope;
import com.tpago.movil.init.intro.IntroFragment;
import com.tpago.movil.init.tutorial.TutorialFragment;
import com.tpago.movil.init.register.RegisterComponent;
import com.tpago.movil.init.register.RegisterModule;
import com.tpago.movil.init.signin.SignInFragment;
import com.tpago.movil.init.signin.SignInPresenter;
import com.tpago.movil.init.unlock.UnlockFragment;
import com.tpago.movil.init.unlock.UnlockPresenter;

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
  void inject(PhoneNumberInitFragment fragment);
  void inject(PhoneNumberInitPresenter presenter);

  void inject(IntroFragment fragment);

  void inject(TutorialFragment fragment);

  void inject(SignInFragment fragment);
  void inject(SignInPresenter presenter);

  void inject(UnlockFragment fragment);
  void inject(UnlockPresenter presenter);
}
